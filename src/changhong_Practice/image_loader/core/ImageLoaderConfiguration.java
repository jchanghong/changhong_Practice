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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.DisplayMetrics;
import changhong_Practice.image_loader.cache.disc.DiscCacheAware;
import changhong_Practice.image_loader.cache.disc.naming.FileNameGenerator;
import changhong_Practice.image_loader.cache.memory.MemoryCacheAware;
import changhong_Practice.image_loader.core.assist.ImageSize;
import changhong_Practice.image_loader.core.assist.QueueProcessingType;
import changhong_Practice.image_loader.core.decode.ImageDecoder;
import changhong_Practice.image_loader.core.download.ImageDownloader;
import changhong_Practice.image_loader.utils.L;
import changhong_Practice.image_loader.utils.StorageUtils;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * Presents configuration for {@link ImageLoader}
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see ImageLoader
 * @see MemoryCacheAware
 * @see DiscCacheAware
 * @see DisplayImageOptions
 * @see ImageDownloader
 * @see FileNameGenerator
 * @since 1.0.0
 */
public final class ImageLoaderConfiguration {

    final Resources resources;
    //定义缓存的大小，
    //当需要显示小图片的时候，直接解码小缓存的图片，不需要压缩
    //当需要大图片显示，直接解码大图片缓存
    //当需要放大图片的时候，解码原图
    //设置static变量，减少函数调用
    //下面的值必须设置，根据这些计算samplesize,
    // 避免根据每imageview计算samplesize
    public static int ImageWidthForSmallDiscCache;
    public static int ImageHeightForSmallDiscCache;
    public static int ImageWidthForBigDiscCache;
    public static int ImageHeightForBigDiscCache;//pix
    public static int maxFileLengthForCache = 200 * 1024;//单位是文件的大小（kb)
    //文件大于这个就缓存，不然直接解码原文件；
    final DiscCacheAware discCacheForBig;//大缓存
    final DiscCacheAware discCacheForSmall;//小缓存


    public static void setMaxFileLengthForCache(
            int maxFileLengthForCache) {
        ImageLoaderConfiguration.maxFileLengthForCache
                = maxFileLengthForCache;
    }

    //end
    final int maxImageWidthForMemoryCache;
    final int maxImageHeightForMemoryCache;
    final CompressFormat imageCompressFormatForDiscCache;
    final int imageQualityForDiscCache;

    final Executor taskExecutor;
    final Executor taskExecutorForCachedImages;
    final boolean customExecutor;
    final boolean customExecutorForCachedImages;

    final int threadPoolSize;
    final int threadPriority;
    final QueueProcessingType tasksProcessingType;
   public final MemoryCacheAware<String, Bitmap> memoryCache;
    final DiscCacheAware discCache;
    final ImageDownloader downloader;
    final ImageDecoder decoder;
    final DisplayImageOptions defaultDisplayImageOptions;
    final boolean writeLogs;
  public static   FileNameGenerator fileNameGenerator;
    final DiscCacheAware reserveDiscCache;

    private ImageLoaderConfiguration(final Builder builder) {
        resources = builder.context.getResources();
        fileNameGenerator=builder.discCacheFileNameGenerator;
        maxImageWidthForMemoryCache = builder.maxImageWidthForMemoryCache;
        maxImageHeightForMemoryCache = builder.maxImageHeightForMemoryCache;
        imageCompressFormatForDiscCache = builder.imageCompressFormatForDiscCache;
        imageQualityForDiscCache = builder.imageQualityForDiscCache;
        taskExecutor = builder.taskExecutor;
        taskExecutorForCachedImages = builder.taskExecutorForCachedImages;
        threadPoolSize = builder.threadPoolSize;
        threadPriority = builder.threadPriority;
        tasksProcessingType = builder.tasksProcessingType;
        discCache = builder.discCache;
        memoryCache = builder.memoryCache;
        defaultDisplayImageOptions = builder.defaultDisplayImageOptions;
        writeLogs = builder.writeLogs;
        downloader = builder.downloader;
        decoder = builder.decoder;

        discCacheForBig = builder.discCachebig;
        discCacheForSmall = builder.discCachesmall;
        ImageHeightForBigDiscCache = builder.ImageHeightForBigDiscCache;
        ImageWidthForBigDiscCache = builder.ImageWidthForBigDiscCache;
        ImageHeightForSmallDiscCache = builder.ImageHeightForSmallDiscCache;
        ImageWidthForSmallDiscCache = builder.ImageWidthForSmallDiscCache;
        customExecutor = builder.customExecutor;
        customExecutorForCachedImages = builder.customExecutorForCachedImages;


        File reserveCacheDir = StorageUtils.getCacheDirectory(builder.context, false);
        reserveDiscCache = DefaultConfigurationFactory.createReserveDiscCache(reserveCacheDir);
        if (ImageHeightForBigDiscCache == 0) {
            ImageHeightForBigDiscCache = getMaxImageSize().getHeight();
        }
        if (ImageWidthForBigDiscCache == 0) {
            ImageWidthForBigDiscCache = getMaxImageSize().getWidth();
        }
    }

    public static ImageLoaderConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }
    ImageSize getMaxImageSize() {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
         int   width = displayMetrics.widthPixels;
         int   height = displayMetrics.heightPixels;
        return new ImageSize(width, height);
    }

    /**
     * Builder for {@link ImageLoaderConfiguration}
     *
     * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
     */
    public static class Builder {
        int ImageWidthForSmallDiscCache = 0;
        int ImageHeightForSmallDiscCache = 0;
        int ImageWidthForBigDiscCache = 0;
        int ImageHeightForBigDiscCache = 0;
        private static final String WARNING_OVERLAP_MEMORY_CACHE = "memoryCache() and memoryCacheSize() calls overlap each other";
        private static final String WARNING_OVERLAP_EXECUTOR = "threadPoolSize(), threadPriority() and tasksProcessingOrder() calls "
                + "can overlap taskExecutor() and taskExecutorForCachedImages() calls.";

        /**
         * {@value}
         */
        public static final int DEFAULT_THREAD_POOL_SIZE = 3;
        /**
         * {@value}
         */
        public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 1;
        /**
         * {@value}
         */
        public static final QueueProcessingType DEFAULT_TASK_PROCESSING_TYPE = QueueProcessingType.FIFO;

        private Context context;

        private int maxImageWidthForMemoryCache = 0;
        private int maxImageHeightForMemoryCache = 0;
        private CompressFormat imageCompressFormatForDiscCache = null;
        private int imageQualityForDiscCache = 0;

        private Executor taskExecutor = null;
        private Executor taskExecutorForCachedImages = null;
        private boolean customExecutor = false;
        private boolean customExecutorForCachedImages = false;

        private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        private int threadPriority = DEFAULT_THREAD_PRIORITY;
        private QueueProcessingType tasksProcessingType = DEFAULT_TASK_PROCESSING_TYPE;

        private int memoryCacheSize = 0;

        private MemoryCacheAware<String, Bitmap> memoryCache = null;
        private DiscCacheAware discCache = null;
        private DiscCacheAware discCachebig = null;
        private DiscCacheAware discCachesmall = null;
        private FileNameGenerator discCacheFileNameGenerator = null;
        private ImageDownloader downloader = null;
        private ImageDecoder decoder;
        private DisplayImageOptions defaultDisplayImageOptions = null;

        private boolean writeLogs = false;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }









        public Builder bigWight(int bigwight) {
            ImageWidthForBigDiscCache = bigwight;
            return this;
        }

        public Builder smallWight(int wight) {
            ImageWidthForSmallDiscCache = wight;
            return this;
        }

        public Builder bigHeigh(int bigheigh) {
            ImageHeightForBigDiscCache = bigheigh;
            return this;
        }

        public Builder samllHeigh(int heigh) {
            ImageHeightForSmallDiscCache = heigh;
            return this;
        }

//        public Builder threadPriority(int threadPriority) {
//            if (taskExecutor != null || taskExecutorForCachedImages != null) {
//                L.w(WARNING_OVERLAP_EXECUTOR);
//            }
//
//            if (threadPriority < Thread.MIN_PRIORITY) {
//                this.threadPriority = Thread.MIN_PRIORITY;
//            } else {
//                if (threadPriority > Thread.MAX_PRIORITY) {
//                    this.threadPriority = Thread.MAX_PRIORITY;
//                } else {
//                    this.threadPriority = threadPriority;
//                }
//            }
//            return this;
//        }
        public Builder tasksProcessingOrder(QueueProcessingType tasksProcessingType) {
            if (taskExecutor != null || taskExecutorForCachedImages != null) {
                L.w(WARNING_OVERLAP_EXECUTOR);
            }

            this.tasksProcessingType = tasksProcessingType;
            return this;
        }

        public Builder memoryCacheSize(int memoryCacheSize) {
            if (memoryCacheSize <= 0) throw new IllegalArgumentException("memoryCacheSize must be a positive number");

            if (memoryCache != null) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE);
            }

            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        /**
         * Sets maximum memory cache size (in percent of available app memory) for {@link android.graphics.Bitmap
         * bitmaps}.<br />
         * Default value - 1/8 of available app memory.<br />
         * <b>NOTE:</b> If you use this method then
         * {@link com.changhong_practice.imageloaderforfile.cache.memory.impl.LruMemoryCache LruMemoryCache} will be used as
         * {@link MemoryCacheAware}.
         */
        public Builder memoryCacheSizePercentage(int availableMemoryPercent) {
            if (availableMemoryPercent <= 0 || availableMemoryPercent >= 100) {
                throw new IllegalArgumentException("availableMemoryPercent must be in range (0 < % < 100)");
            }

            if (memoryCache != null) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE);
            }

            long availableMemory = Runtime.getRuntime().maxMemory();
            memoryCacheSize = (int) (availableMemory * (availableMemoryPercent / 100f));
            return this;
        }


        /**
         * Enables detail logging of {@link ImageLoader} work. To prevent detail logs don't call this method.
         * Consider {@link com.changhong_practice.imageloaderforfile.utils.L#disableLogging()} to disable ImageLoader logging completely (even error logs)
         */
        public Builder writeDebugLogs() {
            this.writeLogs = true;
            return this;
        }

        /**
         * Builds configured {@link ImageLoaderConfiguration} object
         */
        public ImageLoaderConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new ImageLoaderConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (taskExecutor == null) {
                taskExecutor = DefaultConfigurationFactory
                        .createExecutor(threadPoolSize, threadPriority, tasksProcessingType);
            } else {
                customExecutor = true;
            }
            if (taskExecutorForCachedImages == null) {
                taskExecutorForCachedImages = DefaultConfigurationFactory
                        .createExecutor(threadPoolSize, threadPriority, tasksProcessingType);
            } else {
                customExecutorForCachedImages = true;
            }

            if (discCacheFileNameGenerator == null) {
                discCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();

            }
            if (discCachebig == null) {

                discCachebig = DefaultConfigurationFactory.
                        createDiscCachebig(context, discCacheFileNameGenerator
                        );
            }
            if (discCachesmall == null) {
                discCachesmall = DefaultConfigurationFactory.
                        createDiscCachesmall(context, discCacheFileNameGenerator
                        );
            }

            if (memoryCache == null) {
                memoryCache = DefaultConfigurationFactory.createMemoryCache(memoryCacheSize);
            }

            if (downloader == null) {
                downloader = DefaultConfigurationFactory.createImageDownloader(context);
            }
            if (decoder == null) {
                decoder = DefaultConfigurationFactory.createImageDecoder(writeLogs);
            }
            if (defaultDisplayImageOptions == null) {
                defaultDisplayImageOptions = DisplayImageOptions.createSimple();
            }
        }
    }
}
