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


import changhong_Practice.image_loader.core.assist.ImageSize;
import changhong_Practice.image_loader.core.imageaware.ImageAware;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Information for load'n'display image task
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see com.changhong_practice.imageloaderforfile.utils.MemoryCacheUtils
 * @see DisplayImageOptions
 * @since 1.3.1
 */
final class ImageLoadingInfo {

    final String uri;
    final String memoryCacheKey;
    final ImageAware imageAware;
    final ImageSize targetSize;
    final DisplayImageOptions options;
    final ReentrantLock loadFromUriLock;
    final boolean isbig;

    public ImageLoadingInfo(String uri, ImageAware imageAware, ImageSize targetSize, String memoryCacheKey,
                            DisplayImageOptions options
            , ReentrantLock loadFromUriLock, boolean isbig
    ) {
        this.uri = uri;
        this.isbig = isbig;
        this.imageAware = imageAware;
        this.targetSize = targetSize;
        this.options = options;
        this.loadFromUriLock = loadFromUriLock;
        this.memoryCacheKey = memoryCacheKey;
    }
}
