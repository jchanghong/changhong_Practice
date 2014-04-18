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
package changhong_Practice.image_loader.core.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import changhong_Practice.image_loader.core.ImageLoaderConfiguration;
import changhong_Practice.image_loader.core.assist.ImageSize;
import changhong_Practice.image_loader.utils.ImageSizeUtils;
import changhong_Practice.image_loader.utils.IoUtils;
import changhong_Practice.image_loader.utils.L;
import changhong_Practice.uitl.LLL;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes images to {@link android.graphics.Bitmap}, scales them to needed size
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see ImageDecodingInfo
 * @since 1.8.3
 */
public class BaseImageDecoder implements ImageDecoder {

    protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";

    protected final boolean loggingEnabled;

    /**
     *                       ImageLoaderConfiguration.Builder#writeDebugLogs()
     *                       ImageLoaderConfiguration.writeDebugLogs()}
     */
    public BaseImageDecoder(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    /**
     * Decodes image from URI into {@link android.graphics.Bitmap}. Image is scaled close to incoming {@linkplain ImageSize target size}
     * during decoding (depend on incoming parameters).
     *
     * @return Decoded bitmap
     * @throws java.io.IOException                   if some I/O exception occurs during image reading
     * @throws UnsupportedOperationException if image URI has unsupported scheme(protocol)
     */
    public Bitmap decode(ImageDecodingInfo decodingInfo)
            throws IOException {
        boolean isbig = decodingInfo.isbig;
        Bitmap decodedBitmap = null;
        InputStream imageStream = decodingInfo.getDownloader().getStream(decodingInfo.getImageUri());
        if (imageStream == null) {
            LLL.i("iamgestream is null");
            return null;
        }
        try {
//            imageStream = resetStream(imageStream, decodingInfo);
            Options options = decodingInfo.decodingOptions;
            options.inSampleSize = getsample(decodingInfo, isbig);
            decodedBitmap = BitmapFactory.
                    decodeStream(imageStream, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.closeSilently(imageStream);
        }

        if (decodedBitmap == null) {
            L.e(ERROR_CANT_DECODE_IMAGE, decodingInfo.getImageUri());

        }
//        LLL.i("in decode() w:" + decodedBitmap.getWidth() + decodingInfo.getImageUri().toString());
        return decodedBitmap;

    }
//
//    protected InputStream getImageStream(ImageDecodingInfo decodingInfo) throws IOException {
//        return decodingInfo.getDownloader().getStream(decodingInfo.getImageUri()
//        );
//    }


    protected int getsample(

            ImageDecodingInfo decodingInfo, boolean isbig) {

        int scale;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(decodingInfo.getImageUri(), options);

        ImageSize srcsize = new ImageSize(options.outWidth, options.outHeight);
        ImageSize tarsize;
        if (isbig == true) {
            tarsize = new ImageSize(ImageLoaderConfiguration.ImageWidthForBigDiscCache,
                    ImageLoaderConfiguration.ImageHeightForBigDiscCache
            );
        } else {
            tarsize = new ImageSize(ImageLoaderConfiguration.ImageWidthForSmallDiscCache,
                    ImageLoaderConfiguration.ImageHeightForSmallDiscCache
            );
        }

        scale = ImageSizeUtils.computeImageSampleSize(srcsize, tarsize, true);
        return scale;
    }

//    protected InputStream resetStream(InputStream imageStream,
//                                      ImageDecodingInfo decodingInfo) throws IOException {
//        try {
//            imageStream.reset();
//        } catch (IOException e) {
//            IoUtils.closeSilently(imageStream);
//            imageStream = getImageStream(decodingInfo);
//        }
//        return imageStream;
//    }
}