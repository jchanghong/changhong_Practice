
package changhong_Practice.config;

import android.content.Context;
import android.provider.MediaStore;
import changhong_Practice.image_loader.core.ImageLoader;

/**
 * Created by 蒋长宏 on 2014/4/1 0001.
 * at jiangchanghong163@163.com
 */
public final class Constants {

    public static Context context;
    public static ImageLoader imageLoader = ImageLoader.getInstance();
	private Constants() {
	}
	public static class Extra {
		public static final String IMAGES = "com.alimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.alimageloader.IMAGE_POSITION";
	}

    public static final int INDEX_ID = 0;
    public static final int INDEX_CAPTION = 1;
    public static final int INDEX_LATITUDE = 2;
    public static final int INDEX_LONGITUDE = 3;
    public static final int INDEX_DATE_TAKEN = 4;
    public static final int INDEX_DATE_MODIFIED = 5;
    public static final int INDEX_DATA = 6;
    public static final int INDEX_BUCKET_ID = 7;
    public static final int INDEX_SIZE_ID = 8;

  public   static final String[] PROJECTION =  {
            MediaStore.Images.ImageColumns._ID,           // 0
            MediaStore.Images.ImageColumns.TITLE,         // 1
            MediaStore.Images.ImageColumns.LATITUDE,      // 2
            MediaStore.Images.ImageColumns.LONGITUDE,     // 3
            MediaStore.Images.ImageColumns.DATE_TAKEN,    // 4
            MediaStore.Images.ImageColumns.DATE_MODIFIED, // 5
            MediaStore.Images.ImageColumns.DATA,          // 8
            MediaStore.Images.ImageColumns.BUCKET_ID,     // 10
            MediaStore.Images.ImageColumns.SIZE,          // 11
    };
}
