/*******************************************************************************
 * Copyright 2013 Sergey Tarasevich
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
package changhong_Practice.image_loader.utils;

import android.opengl.GLES10;
import changhong_Practice.image_loader.core.assist.ImageSize;

import javax.microedition.khronos.opengles.GL10;

/**
 * Provides calculations with image sizes, scales
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.3
 */
public final class ImageSizeUtils {

    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;

    private static ImageSize maxBitmapSize;

    static {
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        int maxBitmapDimension = Math.max(maxTextureSize[0], DEFAULT_MAX_BITMAP_DIMENSION);
        maxBitmapSize = new ImageSize(maxBitmapDimension, maxBitmapDimension);
    }

    private ImageSizeUtils() {
    }
    public static int computeImageSampleSize(ImageSize srcSize,
                                             ImageSize targetSize,
                                             boolean powerOf2Scale) {
        int srcWidth = srcSize.getWidth();
        int srcHeight = srcSize.getHeight();
        int targetWidth = targetSize.getWidth();
        int targetHeight = targetSize.getHeight();

        int scale = 1;

        int widthScale = srcWidth / targetWidth;
        int heightScale = srcHeight / targetHeight;


        if (powerOf2Scale) {
            while (srcWidth / 2 >= targetWidth && srcHeight / 2 >= targetHeight) { // &&
                srcWidth /= 2;
                srcHeight /= 2;
                scale *= 2;
            }
        } else {
            scale = Math.min(widthScale, heightScale); // min
        }
        if (scale < 1) {
            scale = 1;
        }
        return scale;
    }

    /**
     * Computes minimal sample size for downscaling image so result image size won't exceed max acceptable OpenGL
     * texture size.<br />
     * We can't create Bitmap in memory with size exceed max texture size (usually this is 2048x2048) so this method
     * calculate minimal sample size which should be applied to image to fit into these limits.
     *
     * @param srcSize Original image size
     * @return Minimal sample size
     */
    public static int computeMinImageSampleSize(ImageSize srcSize) {
        int srcWidth = srcSize.getWidth();
        int srcHeight = srcSize.getHeight();
        int targetWidth = maxBitmapSize.getWidth();
        int targetHeight = maxBitmapSize.getHeight();

        int widthScale = (int) Math.ceil((float) srcWidth / targetWidth);
        int heightScale = (int) Math.ceil((float) srcHeight / targetHeight);

        return Math.max(widthScale, heightScale); // max
    }

    /**
     * Computes scale of target size (<b>targetSize</b>) to source size (<b>srcSize</b>).<br />
     * <br />
     * <b>Examples:</b><br />
     * <p/>
     * <pre>
     * srcSize(40x40), targetSize(10x10) -> scale = 0.25
     *
     * srcSize(10x10), targetSize(20x20), stretch = false -> scale = 1
     * srcSize(10x10), targetSize(20x20), stretch = true  -> scale = 2
     *
     * srcSize(100x100), targetSize(20x40), viewScaleType = FIT_INSIDE -> scale = 0.2
     * srcSize(100x100), targetSize(20x40), viewScaleType = CROP       -> scale = 0.4
     * </pre>
     *
     * @param srcSize       Source (image) size
     * @param targetSize    Target (view) size
     * @param viewScaleType {@linkplain ViewScaleType Scale type} for placing image in view
     * @param stretch       Whether source size should be stretched if target size is larger than source size. If <b>false</b>
     *                      then result scale value can't be greater than 1.
     * @return Computed scale
     */
//    public static float computeImageScale(ImageSize srcSize,
//                                          ImageSize targetSize,
//                                          boolean stretch) {
//        int srcWidth = srcSize.getWidth();
//        int srcHeight = srcSize.getHeight();
//        int targetWidth = targetSize.getWidth();
//        int targetHeight = targetSize.getHeight();
//
//        float widthScale = (float) srcWidth / targetWidth;
//        float heightScale = (float) srcHeight / targetHeight;
//
//        int destWidth;
//        int destHeight;
//        if ((viewScaleType == ViewScaleType.FIT_INSIDE && widthScale >= heightScale) || (viewScaleType == ViewScaleType.CROP && widthScale < heightScale)) {
//            destWidth = targetWidth;
//            destHeight = (int) (srcHeight / widthScale);
//        } else {
//            destWidth = (int) (srcWidth / heightScale);
//            destHeight = targetHeight;
//        }
//
//        float scale = 1;
//        if ((!stretch && destWidth < srcWidth && destHeight < srcHeight) || (stretch && destWidth != srcWidth && destHeight != srcHeight)) {
//            scale = (float) destWidth / srcWidth;
//        }
//
//        return scale;
//    }
}
