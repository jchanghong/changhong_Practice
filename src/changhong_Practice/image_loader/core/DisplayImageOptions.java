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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import changhong_Practice.image_loader.core.display.BitmapDisplayer;

/**
 * Contains options for image display. Defines:
 * <ul>
 * <li>whether stub image will be displayed in {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware
 * image aware view} during image loading</li>
 * <li>whether stub image will be displayed in {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware
 * image aware view} if empty URI is passed</li>
 * <li>whether stub image will be displayed in {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware
 * image aware view} if image loading fails</li>
 * <li>whether {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware image aware view} should be reset
 * before image loading start</li>
 * <li>whether loaded image will be cached in memory</li>
 * <li>whether loaded image will be cached on disc</li>
 * <li>image scale type</li>
 * <li>decoding options (including bitmap decoding configuration)</li>
 * <li>delay before loading of image</li>
 * <li>whether consider EXIF parameters of image</li>
 * <li>pre-processor for image Bitmap (before caching in memory)</li>
 * <li>post-processor for image Bitmap (after caching in memory, before displaying)</li>
 * <li>how decoded {@link android.graphics.Bitmap} will be displayed</li>
 * </ul>
 * <p/>
 * You can create instance:
 * <ul>
 * <li>with {@link Builder}:<br />
 * <b>i.e.</b> :
 * {@link Builder#showImageOnLoading(int) showImageOnLoading()}.{@link Builder#build() build()}</code><br />
 * </li>
 * <li>or by static method: {@link #createSimple()}</li> <br />
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public final class DisplayImageOptions {

    private final int imageResOnLoading;
    private final Drawable imageOnLoading;
    private final boolean resetViewBeforeLoading;
    private final Options decodingOptions;
    private final BitmapDisplayer displayer;
    private final Handler handler;

    private DisplayImageOptions(Builder builder) {
        imageResOnLoading = builder.imageResOnLoading;
        imageOnLoading = builder.imageOnLoading;
        resetViewBeforeLoading = builder.resetViewBeforeLoading;
        decodingOptions = builder.decodingOptions;
        displayer = builder.displayer;
        handler = builder.handler;
    }

    public boolean shouldShowImageOnLoading() {
        return imageOnLoading != null || imageResOnLoading != 0;
    }


    public Drawable getImageOnLoading(Resources res) {
        return imageResOnLoading != 0 ? res.getDrawable(imageResOnLoading) : imageOnLoading;
    }


    public boolean isResetViewBeforeLoading() {
        return resetViewBeforeLoading;
    }


    public Options getDecodingOptions() {
        return decodingOptions;
    }


    public BitmapDisplayer getDisplayer() {
        return displayer;
    }

    public Handler getHandler() {
        return handler;
    }


    public static class Builder {
        private int imageResOnLoading = 0;
        private Drawable imageOnLoading = null;
        private boolean resetViewBeforeLoading = false;
        private Options decodingOptions = new Options();
        private BitmapDisplayer displayer = DefaultConfigurationFactory.createBitmapDisplayer();
        private Handler handler = null;

        public Builder() {
            decodingOptions.inPurgeable = true;
//			decodingOptions.inPurgeable = false;
            decodingOptions.inInputShareable = true;
        }

        /**
         * Incoming image will be displayed in {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware
         * image aware view} during image loading
         */
        public Builder showImageOnLoading(int imageRes) {
            imageResOnLoading = imageRes;
            return this;
        }

        /**
         * Incoming drawable will be displayed in {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware
         * image aware view} during image loading.
         * This option will be ignored if {@link DisplayImageOptions.Builder#showImageOnLoading(int)} is set.
         */
        public Builder showImageOnLoading(Drawable drawable) {
            imageOnLoading = drawable;
            return this;
        }



        /**
         * Sets whether {@link com.changhong_practice.imageloaderforfile.core.imageaware.ImageAware
         * image aware view} will be reset (set <b>null</b>) before image loading start
         */
        public Builder resetViewBeforeLoading(boolean resetViewBeforeLoading) {
            this.resetViewBeforeLoading = resetViewBeforeLoading;
            return this;
        }


        /**
         * Sets {@link android.graphics.Bitmap.Config bitmap config} for image decoding. Default value - {@link android.graphics.Bitmap.Config#ARGB_8888}
         */
        public Builder bitmapConfig(Bitmap.Config bitmapConfig) {
            if (bitmapConfig == null) throw new IllegalArgumentException("bitmapConfig can't be null");
            decodingOptions.inPreferredConfig = bitmapConfig;
            return this;
        }



        /**
         * Builds configured {@link DisplayImageOptions} object
         */
        public DisplayImageOptions build() {
            return new DisplayImageOptions(this);
        }
    }

    /**
     * Creates options appropriate for single displaying:
     * <ul>
     * <li>View will <b>not</b> be reset before loading</li>
     * <li>Loaded image will <b>not</b> be cached in memory</li>
     * <li>Loaded image will <b>not</b> be cached on disc</li>
     * <li>{@link android.graphics.Bitmap.Config#ARGB_8888} bitmap config will be used for image decoding</li>
     * <li>{@link SimpleBitmapDisplayer} will be used for image displaying</li>
     * </ul>
     * <p/>
     * These option are appropriate for simple single-use image (from drawables or from Internet) displaying.
     */
    public static DisplayImageOptions createSimple() {
        return new Builder().build();
    }
}
