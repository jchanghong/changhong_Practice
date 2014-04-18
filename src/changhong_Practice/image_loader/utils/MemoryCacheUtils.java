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
package changhong_Practice.image_loader.utils;


import changhong_Practice.image_loader.core.ImageLoader;
import changhong_Practice.image_loader.core.ImageLoaderConfiguration;
import changhong_Practice.image_loader.core.assist.ImageSize;

/**
 * Utility for generating of keys for memory cache, key comparing and other work with memory cache
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.6.3
 */
public final class MemoryCacheUtils {

    private static final String URI_AND_SIZE_SEPARATOR = "_";
    private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

    private MemoryCacheUtils() {
    }

    /**
     * Generates key for memory cache for incoming image (URI + size).<br />
     * Pattern for cache key - <b>[imageUri]_[width]x[height]</b>.
     */
    public static String generateKey(String imageUri, ImageSize targetSize) {
        return new StringBuilder(imageUri).append(URI_AND_SIZE_SEPARATOR).append(targetSize.getWidth()).append(WIDTH_AND_HEIGHT_SEPARATOR).append(targetSize.getHeight()).toString();
    }

    public static final void delete(String path,boolean isbig) {
        ImageSize targetSize;
        if (isbig==true)
            targetSize = new ImageSize(ImageLoaderConfiguration.ImageWidthForBigDiscCache,
                    ImageLoaderConfiguration.ImageHeightForBigDiscCache);
        else
            targetSize = new ImageSize(ImageLoaderConfiguration.ImageWidthForSmallDiscCache,
                    ImageLoaderConfiguration.ImageHeightForSmallDiscCache);
        String memoryCacheKey = MemoryCacheUtils.generateKey(path, targetSize);
        ImageLoader.memoryCacheAware.remove(memoryCacheKey);
    }
}
