package changhong_Practice.config;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by 蒋长宏 on 2014/4/1 0001.
 * at jiangchanghong163@163.com
 */
public class Viewsize {

    private Viewsize() {

    }
    public static Size getbigImageSize(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new Size(width/2, height/2);

    }
    public static Size getSmallImageSize(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new Size(width/6, height/6);

    }

}
