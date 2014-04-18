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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import changhong_Practice.image_loader.cache.disc.DiscCacheAware;
import changhong_Practice.image_loader.core.decode.ImageDecoder;
import changhong_Practice.image_loader.core.decode.ImageDecodingInfo;
import changhong_Practice.image_loader.core.download.ImageDownloader;
import changhong_Practice.image_loader.core.imageaware.ImageAware;
import changhong_Practice.image_loader.utils.IoUtils;
import changhong_Practice.image_loader.utils.L;
import changhong_Practice.uitl.LLL;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Presents load'n'display image task. Used to load image from Internet or file system, decode it to {@link android.graphics.Bitmap}, and
 * display it in {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware} using {@link DisplayBitmapTask}.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see ImageLoaderConfiguration
 * @see ImageLoadingInfo
 * @since 1.3.1
 */
final class LoadAndDisplayImageTask implements Runnable {

    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from orignal file [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISC_CACHE = "Load image from disc cache [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISC = "Cache image on disc [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";

//    private static final int BUFFER_SIZE = 32 * 1024; // 32 Kb

    private final ImageLoaderEngine engine;
    private final ImageLoadingInfo imageLoadingInfo;
    private final Handler handler;

    // Helper references
    private final ImageLoaderConfiguration configuration;
    private final ImageDownloader downloader;
    private final ImageDecoder decoder;
    private final boolean writeLogs;
    final String uri;
    private final String memoryCacheKey;
    final ImageAware imageAware;
    final DisplayImageOptions options;

    boolean isbig;
    DiscCacheAware discCache;

    // State vars
    public LoadAndDisplayImageTask(ImageLoaderEngine engine,
                                   ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.engine = engine;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
        this.isbig = imageLoadingInfo.isbig;
        configuration = engine.configuration;
        downloader = configuration.downloader;
        decoder = configuration.decoder;
        writeLogs = configuration.writeLogs;
        uri = imageLoadingInfo.uri;
        memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        imageAware = imageLoadingInfo.imageAware;
        options = imageLoadingInfo.options;
        if (isbig) {
            discCache = configuration.discCacheForBig;
        } else {
            discCache = configuration.discCacheForSmall;
        }
    }

    @Override
    public void run() {
        if (waitIfPaused()) return;

        ReentrantLock loadFromUriLock = imageLoadingInfo.loadFromUriLock;
        log(LOG_START_DISPLAY_IMAGE_TASK);
        if (loadFromUriLock.isLocked()) {
            log(LOG_WAITING_FOR_IMAGE_LOADED);
        }

        loadFromUriLock.lock();
        Bitmap bmp = null;
        try {
            checkTaskNotActual();

//            bmp = configuration.memoryCache.get(memoryCacheKey);
//            if (bmp == null) {
            bmp = tryLoadBitmap();
            if (bmp == null) return; // listener callback already was fired
            checkTaskNotActual();
            checkTaskInterrupted();
            if (bmp != null) {
                log(LOG_CACHE_IMAGE_IN_MEMORY);
                configuration.memoryCache.put(memoryCacheKey, bmp);
            }
//            }
            checkTaskNotActual();
            checkTaskInterrupted();
        } catch (TaskCancelledException e) {
            return;
        } finally {
            loadFromUriLock.unlock();
        }

        DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp,
                imageLoadingInfo, engine);
        displayBitmapTask.setLoggingEnabled(writeLogs);
        runTask(displayBitmapTask, handler, engine);
    }

    /**
     * @return <b>true</b> - if task should be interrupted; <b>false</b> - otherwise
     */
    private boolean waitIfPaused() {
        AtomicBoolean pause = engine.getPause();
        if (pause.get()) {
            synchronized (engine.getPauseLock()) {
                if (pause.get()) {
                    log(LOG_WAITING_FOR_RESUME);
                    try {
                        engine.getPauseLock().wait();
                    } catch (InterruptedException e) {
                        L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
                        return true;
                    }
                    log(LOG_RESUME_AFTER_PAUSE);
                }
            }
        }
        return isTaskNotActual();
    }


    private Bitmap tryLoadBitmap() throws TaskCancelledException {
        File imageFile = discCache.get(uri);
        Bitmap bitmap = null;
        try {
            if (imageFile.exists()) {
                log(LOG_LOAD_IMAGE_FROM_DISC_CACHE + imageFile.getAbsolutePath());
                checkTaskNotActual();
//                Log.i("changhong", "load form disc cache" + imageFile.getAbsolutePath());
//                bitmap = decodeImage(imageFile.getAbsolutePath());
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options.getDecodingOptions());
            }
            if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                log(LOG_LOAD_IMAGE_FROM_NETWORK + uri);
//                Log.i("changhong", "load form orig file" + uri);
                bitmap = decodeImage(uri);
                if (bitmap!=null)
               tryCacheImageOnDisc(bitmap, imageFile);
                checkTaskNotActual();
            }
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (TaskCancelledException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            L.e(e);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            L.e(e);
        } catch (Throwable e) {
            e.printStackTrace();
            L.e(e);
        }
        return bitmap;
    }


    private Bitmap decodeImage(String imageUri) throws IOException {
        ImageDecodingInfo decodingInfo = new ImageDecodingInfo(memoryCacheKey,
                imageUri,
                downloader, options, isbig);
        return decoder.decode(decodingInfo);
    }

    /**
     * @return <b>true</b> - if image was downloaded successfully; <b>false</b> - otherwise
     */
    private boolean tryCacheImageOnDisc(Bitmap bitmap, File targetFile)
            throws TaskCancelledException {
        log(LOG_CACHE_IMAGE_ON_DISC);
        boolean loaded = false;
        try {
            BufferedOutputStream outputStream =
                    new BufferedOutputStream(new FileOutputStream(targetFile), 32 * 1024);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            IoUtils.closeSilently(outputStream);
            loaded = true;
        } catch (IOException e) {
            e.printStackTrace();
            L.e(e);
            if (targetFile.exists()) {
                targetFile.delete();
            }
        }
        return loaded;
    }

//    private boolean downloadImage(File targetFile) throws IOException {
//        InputStream is = downloader.getStream(uri);
//        boolean loaded;
//        try {
//            OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
//            try {
//                loaded = IoUtils.copyStream(is, os);
//            } finally {
//                IoUtils.closeSilently(os);
//            }
//        } finally {
//            IoUtils.closeSilently(is);
//        }
//        return loaded;
//    }

    /**
     * Decodes image file into Bitmap, resize it and save it back
     */
//    private boolean resizeAndSaveImage(File targetFile, int maxWidth, int maxHeight) throws IOException {
//        boolean saved = false;
//        // Decode image file, compress and re-save it
//        ImageSize targetImageSize = new ImageSize(maxWidth, maxHeight);
//        Log.i("changhong", "resizeAndSaveImage targsize:" + targetImageSize.toString());
//        DisplayImageOptions specialOptions = new DisplayImageOptions.Builder().cloneFrom(options)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
//        ImageDecodingInfo decodingInfo = new ImageDecodingInfo(memoryCacheKey,
//                Scheme.FILE.wrap(targetFile.getAbsolutePath()), targetImageSize, ViewScaleType.FIT_INSIDE,
//                getDownloader(), specialOptions);
//        Bitmap bmp = decoder.decode(decodingInfo);
//
//        if (bmp != null) {
//            OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
//            try {
//
//                bmp.compress(configuration.imageCompressFormatForDiscCache, configuration.imageQualityForDiscCache, os);
//            } finally {
//                IoUtils.closeSilently(os);
//            }
//            bmp.recycle();
//        }
//        return true;
//    }

    /**
     * @throws TaskCancelledException if task is not actual (target ImageAware is collected by GC or the image URI of
     *                                this task doesn't match to image URI which is actual for current ImageAware at
     *                                this moment)
     */
    private void checkTaskNotActual() throws TaskCancelledException {
        checkViewCollected();
        checkViewReused();
    }

    /**
     * @return <b>true</b> - if task is not actual (target ImageAware is collected by GC or the image URI of this task
     * doesn't match to image URI which is actual for current ImageAware at this moment)); <b>false</b> - otherwise
     */
    private boolean isTaskNotActual() {
        return isViewCollected() || isViewReused();
    }

    /**
     * @throws TaskCancelledException if target ImageAware is collected
     */
    private void checkViewCollected() throws TaskCancelledException {
        if (isViewCollected()) {
            throw new TaskCancelledException();
        }
    }

    /**
     * @return <b>true</b> - if target ImageAware is collected by GC; <b>false</b> - otherwise
     */
    private boolean isViewCollected() {
        if (imageAware.isCollected()) {
            log(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED);
            return true;
        }
        return false;
    }

    /**
     * @throws TaskCancelledException if target ImageAware is collected by GC
     */
    private void checkViewReused() throws TaskCancelledException {
        if (isViewReused()) {
            throw new TaskCancelledException();
        }
    }

    /**
     * @return <b>true</b> - if current ImageAware is reused for displaying another image; <b>false</b> - otherwise
     */
    private boolean isViewReused() {
        String currentCacheKey = engine.getLoadingUriForView(imageAware);
        // Check whether memory cache key (image URI) for current ImageAware is actual.
        // If ImageAware is reused for another task then current task should be cancelled.
        boolean imageAwareWasReused = !memoryCacheKey.equals(currentCacheKey);
        if (imageAwareWasReused) {
            log(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED);
            return true;
        }
        return false;
    }

    /**
     * @throws TaskCancelledException if current task was interrupted
     */
    private void checkTaskInterrupted() throws TaskCancelledException {
        if (isTaskInterrupted()) {
            throw new TaskCancelledException();
        }
    }

    /**
     * @return <b>true</b> - if current task was interrupted; <b>false</b> - otherwise
     */
    private boolean isTaskInterrupted() {
        if (Thread.interrupted()) {
            log(LOG_TASK_INTERRUPTED);
            return true;
        }
        return false;
    }

    String getLoadingUri() {
        return uri;
    }

    private void log(String message) {
        if (writeLogs) L.d(message, memoryCacheKey+uri);
    }

//    private void log(String message, Object... args) {
//        if (writeLogs) L.d(message, args);
//    }

    static void runTask(Runnable r, Handler handler, ImageLoaderEngine engine) {

        if (handler == null) {
            engine.fireCallback(r);
        } else {
            handler.post(r);
        }
    }

    /**
     * Exceptions for case when task is cancelled (thread is interrupted, image view is reused for another task, view is
     * collected by GC).
     *
     * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
     * @since 1.9.1
     */
    class TaskCancelledException extends Exception {
    }
}