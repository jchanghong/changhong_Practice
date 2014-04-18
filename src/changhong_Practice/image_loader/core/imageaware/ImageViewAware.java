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
package changhong_Practice.image_loader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.widget.ImageView;
import changhong_Practice.image_loader.utils.L;

/**
 用强引用
 避免问题
 */
public class ImageViewAware implements ImageAware {

	public static final String WARN_CANT_SET_DRAWABLE = "Can't set a drawable into view. You should call ImageLoader on UI thread for it.";
	public static final String WARN_CANT_SET_BITMAP = "Can't set a bitmap into view. You should call ImageLoader on UI thread for it.";

	protected ImageView imageViewRef;
	protected boolean checkActualViewSize;

	public ImageViewAware(ImageView imageView) {
		this(imageView, true);
	}


	public ImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        this.imageViewRef = imageView;
        this.checkActualViewSize = checkActualViewSize;
    }

    @Override
	public ImageView getWrappedView() {
		return imageViewRef;
	}

	@Override
	public boolean isCollected() {
		return imageViewRef == null;
	}

	@Override
	public int getId() {
		return imageViewRef == null ? super.hashCode() : imageViewRef.hashCode();
	}
	@Override
	public boolean setImageDrawable(Drawable drawable) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			if (imageViewRef != null) {
				imageViewRef.setImageDrawable(drawable);
				return true;
			}
		} else {
			L.w(WARN_CANT_SET_DRAWABLE);
		}
		return false;
	}

	@Override
	public boolean setImageBitmap(Bitmap bitmap) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			if (imageViewRef != null) {
				imageViewRef.setImageBitmap(bitmap);
				return true;
			}
		} else {
			L.w(WARN_CANT_SET_BITMAP);
		}
		return false;
	}
}
