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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import changhong_Practice.image_loader.cache.disc.naming.FileNameGenerator;
import changhong_Practice.image_loader.cache.memory.MemoryCacheAware;
import changhong_Practice.image_loader.core.assist.ImageSize;
import changhong_Practice.image_loader.core.imageaware.ImageAware;
import changhong_Practice.image_loader.core.imageaware.ImageViewAware;
import changhong_Practice.image_loader.utils.L;
import changhong_Practice.image_loader.utils.MemoryCacheUtils;

public class ImageLoader {

    public static final String TAG = ImageLoader.class.getSimpleName();

    static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
    static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";

    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize ImageLoader which had already been initialized before. " + "To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.";
    private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";

    private ImageLoaderConfiguration configuration;
    private ImageLoaderEngine engine;
    private volatile static ImageLoader instance;

    /**
     * Returns singleton class instance
     */
    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    public static FileNameGenerator fileNameGenerator;
    public static MemoryCacheAware<String, Bitmap> memoryCacheAware;
    public static boolean isbig;

    protected ImageLoader() {
    }

    public synchronized void init(ImageLoaderConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        }
        if (this.configuration == null) {
            if (configuration.writeLogs) L.d(LOG_INIT_CONFIG);
            engine = new ImageLoaderEngine(configuration);
            this.configuration = configuration;
            fileNameGenerator = configuration.fileNameGenerator;
            memoryCacheAware = configuration.memoryCache;
        } else {
            L.w(WARNING_RE_INIT_CONFIG);
        }
    }

    public void displayImage(String uri,
                             ImageView imageView,
                             DisplayImageOptions options, boolean isBigView
    ) {
        this.isbig = isBigView;
        ImageAware imageAware = new ImageViewAware(imageView);
        if (options == null) {
            options = configuration.defaultDisplayImageOptions;
        }
        ImageSize targetSize;//= ImageSizeUtils.defineTargetSizeForView(imageAware, configuration.getMaxImageSize());
        if (isbig == true)
            targetSize = new ImageSize(ImageLoaderConfiguration.ImageWidthForBigDiscCache,
                    ImageLoaderConfiguration.ImageHeightForBigDiscCache);
        else
            targetSize = new ImageSize(ImageLoaderConfiguration.ImageWidthForSmallDiscCache,
                    ImageLoaderConfiguration.ImageHeightForSmallDiscCache);
        String memoryCacheKey = MemoryCacheUtils.generateKey(uri, targetSize);
        engine.prepareDisplayTaskFor(imageAware, memoryCacheKey);


        Bitmap bmp = configuration.memoryCache.get(memoryCacheKey);
        if (bmp != null && !bmp.isRecycled()) {
//            if (configuration.writeLogs)
//                L.d(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, memoryCacheKey);
            Log.i("changhong", LOG_LOAD_IMAGE_FROM_MEMORY_CACHE + memoryCacheKey);
            options.getDisplayer().display(bmp, imageAware);
        } else {
            if (options.shouldShowImageOnLoading()) {
                imageAware.setImageDrawable(options.getImageOnLoading(configuration.resources));
            } else if (options.isResetViewBeforeLoading()) {
                imageAware.setImageDrawable(null);
            }

            ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo(uri, imageAware,
                    targetSize, memoryCacheKey,
                    options, engine.getLockForUri(uri), isBigView);
            LoadAndDisplayImageTask displayTask = new LoadAndDisplayImageTask(engine,
                    imageLoadingInfo,
                    defineHandler(options));

            engine.submit(displayTask, isBigView);
        }
    }

    private void checkConfiguration() {
        if (configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    public void clearMemoryCache() {
        checkConfiguration();
        configuration.memoryCache.clear();
    }

    public void clearDiscCache() {
        checkConfiguration();
        configuration.discCacheForBig.clear();
        configuration.discCacheForSmall.clear();
    }

    public void pause() {
        engine.pause();
    }

    public void resume() {
        engine.resume();
    }

    public void stop() {
        engine.stop();
    }

    private static Handler defineHandler(DisplayImageOptions options) {
        Handler handler = options.getHandler();

        if (handler == null && Looper.myLooper() == Looper.getMainLooper()) {
            handler = new Handler();
        }
        return handler;
    }
}
