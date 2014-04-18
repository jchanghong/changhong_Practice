package changhong_Practice.image_operate;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.widget.BaseAdapter;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.utils.DiscCacheUtils;
import changhong_Practice.image_loader.utils.MemoryCacheUtils;
import changhong_Practice.image_object.TimeImage;
import changhong_Practice.uitl.LLL;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by 长宏 on 2014/4/1 0001.
 */
public class Delete {
    public static final int HANDER_WHAT_DELETED_IMAGE = 111;
    private Delete() {

    }
    final static String SELECTION = MediaStore.Images.ImageColumns.DATA
            + " = " + "?";
    public static void lauchFileScan() {
        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Constants.context.sendBroadcast(mediaScanIntent);
    }

    public static void delete(final BaseAdapter baseAdapter, final List<String> images,
                              final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String path = images.remove(position);
                LLL.i("delete " + path);
                String[] str = new String[]{path};
                ContentResolver contentResolver = Constants.context.getContentResolver();
                contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, SELECTION, str);
                File file = new File(path);
                file.delete();
                lauchFileScan();
            }
        };
        Threadpool.sub(runnable);
    }

    public static void delete(final BaseAdapter baseAdapter, final List<TimeImage> images,
                              final Set<String> strings) {
        LLL.i("delete  " + strings);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = Constants.context.getContentResolver();
                for (String s : strings) {
//                    images.remove(s);
                    String[] str = new String[]{s};
                    contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, SELECTION, str);
                    MemoryCacheUtils.delete(s, false);
//                    DiscCacheUtils.delete(s);
                    File file = new File(s);
                    file.delete();
                }
            }
        };
        Threadpool.sub(runnable);
    }

    public static void delete(final PagerAdapter baseAdapter, final List<TimeImage> images,
                              final int position) {
                TimeImage timeImage = images.remove(position);
                String[] str = new String[]{timeImage.path};
                ContentResolver contentResolver = Constants.context.getContentResolver();
                contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, SELECTION, str);
                MemoryCacheUtils.delete(timeImage.path, true);
    }

    public static void delete(final TimeImage  timeImage, final boolean isbig) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, timeImage.id);
                LLL.i("delete"+uri.toString());
                ContentResolver contentResolver = Constants.context.getContentResolver();
                contentResolver.delete(uri, null, null);
                MemoryCacheUtils.delete(timeImage.path, isbig);
                DiscCacheUtils.delete(timeImage.path);
//                lauchFileScan();
            }
        };
        Threadpool.sub(runnable);
    }

    public static void delete(List<String> paths) {

    }

    public static void delete(long ids) {

    }
}
