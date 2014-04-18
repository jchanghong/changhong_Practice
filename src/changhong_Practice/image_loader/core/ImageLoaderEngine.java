/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package changhong_Practice.image_loader.core;


import changhong_Practice.image_loader.cache.disc.DiscCacheAware;
import changhong_Practice.image_loader.core.imageaware.ImageAware;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link ImageLoader} engine which responsible for {@linkplain LoadAndDisplayImageTask display task} execution.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.7.1
 */
class ImageLoaderEngine {

    final ImageLoaderConfiguration configuration;

    private Executor taskExecutor;
    private Executor taskExecutorForCachedImages;
    private Executor taskDistributor;

    private final Map<Integer, String> cacheKeysForImageAwares = Collections
            .synchronizedMap(new HashMap<Integer, String>());
    private final Map<String, ReentrantLock> uriLocks = new WeakHashMap<String, ReentrantLock>();

    private final AtomicBoolean paused = new AtomicBoolean(false);

    DiscCacheAware discCache;
    private final Object pauseLock = new Object();

    ImageLoaderEngine(ImageLoaderConfiguration configuration) {
        this.configuration = configuration;

        taskExecutor = configuration.taskExecutor;
        taskExecutorForCachedImages = configuration.taskExecutorForCachedImages;

        taskDistributor = Executors.newCachedThreadPool();
    }

    /** Submits task to execution pool */
    void submit(final LoadAndDisplayImageTask task, final boolean isbig) {
        taskDistributor.execute(new Runnable() {
            @Override
            public void run() {
                if (isbig) {
                    discCache = configuration.discCacheForBig;
                } else {
                    discCache = configuration.discCacheForSmall;
                }
                boolean isImageCachedOnDisc = discCache.get(task.getLoadingUri()).exists();
                initExecutorsIfNeed();
                if (isImageCachedOnDisc) {
                    taskExecutorForCachedImages.execute(task);
                } else {
                    taskExecutor.execute(task);
                }
            }
        });
    }



    private void initExecutorsIfNeed() {
        if ( ((ExecutorService) taskExecutor).isShutdown()) {
            taskExecutor = createTaskExecutor();
        }
        if (((ExecutorService) taskExecutorForCachedImages)
                .isShutdown()) {
            taskExecutorForCachedImages = createTaskExecutor();
        }
    }

    private Executor createTaskExecutor() {
        return DefaultConfigurationFactory
                .createExecutor(configuration.threadPoolSize, configuration.threadPriority,
                        configuration.tasksProcessingType);
    }

    /**
     * Returns URI of image which is loading at this moment into passed {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware}
     */
    String getLoadingUriForView(ImageAware imageAware) {
        return cacheKeysForImageAwares.get(imageAware.getId());
    }

    /**
     * Associates <b>memoryCacheKey</b> with <b>imageAware</b>. Then it helps to define image URI is loaded into View at
     * exact moment.
     */
    void prepareDisplayTaskFor(ImageAware imageAware, String memoryCacheKey) {
        cacheKeysForImageAwares.put(imageAware.getId(), memoryCacheKey);
    }

    /**
     * Cancels the task of loading and displaying image for incoming <b>imageAware</b>.
     *
     * @param imageAware {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware} for which display task
     *                   will be cancelled
     */
    void cancelDisplayTaskFor(ImageAware imageAware) {
        cacheKeysForImageAwares.remove(imageAware.getId());
    }




    /**
     * Pauses engine. All new "load&display" tasks won't be executed until ImageLoader is {@link #resume() resumed}.<br
     * /> Already running tasks are not paused.
     */
    void pause() {
        paused.set(true);
    }

    /** Resumes engine work. Paused "load&display" tasks will continue its work. */
    void resume() {
        paused.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    /** Stops engine, cancels all running and scheduled display image tasks. Clears internal data. */
    void stop() {
        if (!configuration.customExecutor) {
            ((ExecutorService) taskExecutor).shutdownNow();
        }
        if (!configuration.customExecutorForCachedImages) {
            ((ExecutorService) taskExecutorForCachedImages).shutdownNow();
        }

        cacheKeysForImageAwares.clear();
        uriLocks.clear();
    }

    void fireCallback(Runnable r) {
        taskDistributor.execute(r);
    }

    ReentrantLock getLockForUri(String uri) {
        ReentrantLock lock = uriLocks.get(uri);
        if (lock == null) {
            lock = new ReentrantLock();
            uriLocks.put(uri, lock);
        }
        return lock;
    }

    AtomicBoolean getPause() {
        return paused;
    }

    Object getPauseLock() {
        return pauseLock;
    }


}
