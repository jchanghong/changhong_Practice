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
import changhong_Practice.image_loader.core.display.BitmapDisplayer;
import changhong_Practice.image_loader.core.imageaware.ImageAware;
import changhong_Practice.image_loader.utils.L;

/**
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see BitmapDisplayer
 * @since 1.3.1
 */
final class DisplayBitmapTask implements Runnable {

    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";

    private final Bitmap bitmap;
    private final ImageAware imageAware;
    private final String memoryCacheKey;
    private final BitmapDisplayer displayer;
    private final ImageLoaderEngine engine;
    private boolean loggingEnabled;
    public DisplayBitmapTask(Bitmap bitmap,
                             ImageLoadingInfo imageLoadingInfo,
                             ImageLoaderEngine engine
    ) {
        this.bitmap = bitmap;
        imageAware = imageLoadingInfo.imageAware;
        memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        displayer = imageLoadingInfo.options.getDisplayer();
        this.engine = engine;
    }

    @Override
    public void run() {
        if (imageAware.isCollected()) {
            if (loggingEnabled) L.d(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, memoryCacheKey);
        } else if (isViewWasReused()) {
            if (loggingEnabled) L.d(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED, memoryCacheKey);
        } else {
            displayer.display(bitmap, imageAware);
            engine.cancelDisplayTaskFor(imageAware);
        }
    }

    /** Checks whether memory cache key (image URI) for current ImageAware is actual */
    private boolean isViewWasReused() {
        String currentCacheKey = engine.getLoadingUriForView(imageAware);
        return !memoryCacheKey.equals(currentCacheKey);
    }

    void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }
}
