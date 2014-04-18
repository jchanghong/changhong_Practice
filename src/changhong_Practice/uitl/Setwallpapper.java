package changhong_Practice.uitl;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import changhong_Practice.image_operate.Threadpool;
import changhong_Practice.ui.SingleImagePagerActivity;

import java.io.IOException;

/**
 * Created by 蒋长宏 on 2014/4/16 0016 15:00
 * at jiangchanghong163@163.com
 */
public class Setwallpapper {
    public static void setwallpapper(final String path, final Context context) {

        Runnable r=new Runnable() {
            @Override
            public void run() {
                SingleImagePagerActivity.handler.sendEmptyMessage(1);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                Context mcontext = context;
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(mcontext);
                try {
                    wallpaperManager.setBitmap(bitmap);
                    SingleImagePagerActivity.handler.sendEmptyMessage(2);
                } catch (IOException e) {
                    e.printStackTrace();
                    SingleImagePagerActivity.handler.sendEmptyMessage(2);

                }
            }
        };
        Threadpool.sub(r);


    }
}


