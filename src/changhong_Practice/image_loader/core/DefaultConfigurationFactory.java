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

import android.content.Context;
import android.graphics.Bitmap;
import changhong_Practice.image_loader.cache.disc.DiscCacheAware;
import changhong_Practice.image_loader.cache.disc.impl.TotalSizeLimitedDiscCache;
import changhong_Practice.image_loader.cache.disc.impl.UnlimitedDiscCache;
import changhong_Practice.image_loader.cache.disc.naming.FileNameGenerator;
import changhong_Practice.image_loader.cache.disc.naming.HashCodeFileNameGenerator;
import changhong_Practice.image_loader.cache.memory.MemoryCacheAware;
import changhong_Practice.image_loader.cache.memory.impl.LRULimitedMemoryCache;
import changhong_Practice.image_loader.cache.memory.impl.LruMemoryCache;
import changhong_Practice.image_loader.core.assist.QueueProcessingType;
import changhong_Practice.image_loader.core.assist.deque.LIFOLinkedBlockingDeque;
import changhong_Practice.image_loader.core.decode.BaseImageDecoder;
import changhong_Practice.image_loader.core.decode.ImageDecoder;
import changhong_Practice.image_loader.core.display.BitmapDisplayer;
import changhong_Practice.image_loader.core.display.SimpleBitmapDisplayer;
import changhong_Practice.image_loader.core.download.BaseImageDownloader;
import changhong_Practice.image_loader.core.download.ImageDownloader;
import changhong_Practice.image_loader.utils.StorageUtils;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory for providing of default options for {@linkplain ImageLoaderConfiguration configuration}
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class DefaultConfigurationFactory {


    /**
     * Creates default implementation of task executor
     */
    public static Executor createExecutor(int threadPoolSize, int threadPriority, QueueProcessingType tasksProcessingType) {
        boolean lifo = tasksProcessingType == QueueProcessingType.LIFO;
        BlockingQueue<Runnable> taskQueue = lifo ? new LIFOLinkedBlockingDeque<Runnable>() : new LinkedBlockingQueue<Runnable>();
        return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue, createThreadFactory(threadPriority));
    }

    /**
     */
    public static FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    public static DiscCacheAware createDiscCachesmall(Context context,FileNameGenerator fileNameGenerator
                                                      ) {

        File cacheDir = StorageUtils.getSmallCacheDirectory(context);
        return new UnlimitedDiscCache(cacheDir,fileNameGenerator);

    }

    public static DiscCacheAware createDiscCachebig(Context context,FileNameGenerator fileNameGenerator
                                                   ) {

        File cacheDir = StorageUtils.getBigCacheDirectory(context);
        return new UnlimitedDiscCache(cacheDir,fileNameGenerator);

    }

    /**
     * Creates reserve disc cache which will be used if primary disc cache becomes unavailable
     */
    public static DiscCacheAware createReserveDiscCache(File cacheDir) {
        File individualDir = new File(cacheDir, "uil-images");
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return new TotalSizeLimitedDiscCache(cacheDir, 2 * 1024 * 1024); // limit - 2 Mb
    }

    /**
     * Creates default implementation of {@link MemoryCacheAware} - {@link LruMemoryCache}<br />
     * Default cache size = 1/8 of available app memory.
     */
    public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize) {
        if (memoryCacheSize == 0) {
            memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        }
        return new LRULimitedMemoryCache(memoryCacheSize);
    }

    /**
     * Creates default implementation of {@link com.changhong_practice.imageloaderforfile.core.download.ImageDownloader} - {@link com.changhong_practice.imageloaderforfile.core.download.BaseImageDownloader}
     */
    public static ImageDownloader createImageDownloader(Context context) {
        return new BaseImageDownloader(context);
    }

    /**
     * Creates default implementation of {@link com.changhong_practice.imageloaderforfile.core.decode.ImageDecoder} - {@link com.changhong_practice.imageloaderforfile.core.decode.BaseImageDecoder}
     */
    public static ImageDecoder createImageDecoder(boolean loggingEnabled) {
        return new BaseImageDecoder(loggingEnabled);
    }

    /**
     * Creates default implementation of {@link com.changhong_practice.imageloaderforfile.core.display.BitmapDisplayer} - {@link com.changhong_practice.imageloaderforfile.core.display.SimpleBitmapDisplayer}
     */
    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }

    /**
     * Creates default implementation of {@linkplain java.util.concurrent.ThreadFactory thread factory} for task executor
     */
    private static ThreadFactory createThreadFactory(int threadPriority) {
        return new DefaultThreadFactory(threadPriority);
    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final int threadPriority;

        DefaultThreadFactory(int threadPriority) {
            this.threadPriority = threadPriority;
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "uil-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) t.setDaemon(false);
            t.setPriority(threadPriority);
            return t;
        }
    }
}
