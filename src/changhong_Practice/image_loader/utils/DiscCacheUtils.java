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


import changhong_Practice.image_loader.cache.disc.naming.FileNameGenerator;
import changhong_Practice.image_loader.core.ImageLoader;

import java.io.File;

/**
 * Utility for generating of keys for memory cache, key comparing and other work with memory cache
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.6.3
 */
public final class DiscCacheUtils {


    private DiscCacheUtils() {
    }

    public static final void delete(String path) {
        FileNameGenerator fileNameGenerator= ImageLoader.fileNameGenerator;
        try {
            File file = new File(fileNameGenerator.generate(path));
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
