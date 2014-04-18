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
package changhong_Practice.image_loader.core.download;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides retrieving of {@link java.io.InputStream} of image by URI from network or file system or app resources.<br />
 * {@link java.net.URLConnection} is used to retrieve image stream from network.
 *  去掉没有用的，只从本地取文件
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public class BaseImageDownloader implements ImageDownloader {
    /** {@value} */
    protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
    /** {@value} */

    protected final Context context;
    public BaseImageDownloader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public InputStream getStream(String imageUri) throws IOException {
        return
                new BufferedInputStream(new FileInputStream(imageUri),
                        BUFFER_SIZE)
                ;
    }
}