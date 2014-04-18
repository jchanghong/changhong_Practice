package com.edmodo.cropper.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

/**
 * Created by Yu on 14-4-11.
 */
public class Wallpapper {
    /**
     * Set the bitmap as the wallpapper.
     * @param path   the file's path you want to use as wallpapper.
     * @param context  Context.
     * @return   Whether the wallpapper set successfully.
     */
    public boolean setwallpapper(String path,Context context){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Context mcontext = context;
        WallpaperManager wallpaperManager =WallpaperManager.getInstance(mcontext);
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
