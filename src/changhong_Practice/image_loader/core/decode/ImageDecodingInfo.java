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
package changhong_Practice.image_loader.core.decode;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import changhong_Practice.image_loader.core.DisplayImageOptions;
import changhong_Practice.image_loader.core.download.ImageDownloader;

/**
 * Contains needed information for decoding image to Bitmap
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.3
 */
public class ImageDecodingInfo {

//    增加大小判断
    public boolean isbig;
	private final String imageKey;
	private final String imageUri;
	private final ImageDownloader downloader;
	public final Options decodingOptions;

	public ImageDecodingInfo(String imageKey,
                             String imageUri,

							 ImageDownloader downloader,
                             DisplayImageOptions displayOptions,boolean isbig) {
		this.imageKey = imageKey;
		this.imageUri = imageUri;
        this.isbig = isbig;

		this.downloader = downloader;

		decodingOptions = new Options();
		copyOptions(displayOptions.getDecodingOptions(), decodingOptions);
	}

	private void copyOptions(Options srcOptions, Options destOptions) {
		destOptions.inDensity = srcOptions.inDensity;
		destOptions.inDither = srcOptions.inDither;
		destOptions.inInputShareable = srcOptions.inInputShareable;
		destOptions.inJustDecodeBounds = srcOptions.inJustDecodeBounds;
		destOptions.inPreferredConfig = srcOptions.inPreferredConfig;
		destOptions.inPurgeable = srcOptions.inPurgeable;
		destOptions.inSampleSize = srcOptions.inSampleSize;
		destOptions.inScaled = srcOptions.inScaled;
		destOptions.inScreenDensity = srcOptions.inScreenDensity;
		destOptions.inTargetDensity = srcOptions.inTargetDensity;
		destOptions.inTempStorage = srcOptions.inTempStorage;
		if (Build.VERSION.SDK_INT >= 10) copyOptions10(srcOptions, destOptions);
		if (Build.VERSION.SDK_INT >= 11) copyOptions11(srcOptions, destOptions);
	}

	@TargetApi(10)
	private void copyOptions10(Options srcOptions, Options destOptions) {
		destOptions.inPreferQualityOverSpeed = srcOptions.inPreferQualityOverSpeed;
	}

	@TargetApi(11)
	private void copyOptions11(Options srcOptions, Options destOptions) {
		destOptions.inBitmap = srcOptions.inBitmap;
		destOptions.inMutable = srcOptions.inMutable;
	}

	/** @return Original {@linkplain com.changhong_practice.imageloaderforfile.utils.MemoryCacheUtils#generateKey(String, com.changhong_practice.imageloaderforfile.core.assist.ImageSize) image key} (used in memory cache). */
	public String getImageKey() {
		return imageKey;
	}

	/** @return Image URI for decoding (usually image from disc cache) */
	public String getImageUri() {
		return imageUri;
	}



	/**
	 * @return {@linkplain ImageScaleType Scale type for image sampling and scaling}. This parameter affects result size
	 * of decoded bitmap.
	 */


	/** @return Downloader for image loading */
	public ImageDownloader getDownloader() {
		return downloader;
	}
}