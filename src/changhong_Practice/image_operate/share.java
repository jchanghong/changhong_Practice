package changhong_Practice.image_operate;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import changhong_Practice.image_object.TimeImage;

import java.util.List;

/**
 * Created by 蒋长宏 on 2014/4/2 0002.
 * at jiangchanghong163@163.com
 */
public class share {
    private share() {

    }

    public static void share(String path) {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("image/*");
//        Uri uri = Uri.fromFile(new File(path));
//
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        SingleImagePagerActivity.shareActionProvider.setShareIntent(shareIntent);
    }

    public static void share(long id) {

    }

    public static final void share(List<String> pths) {

    }

    public static final void share(long[] ids) {

    }
    public static final void share(TimeImage timeImage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, timeImage.id);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        SingleImagePagerActivity.shareActionProvider.setShareIntent(shareIntent);
    }


    private Intent createShareIntent() {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("image/*");
//        ShareActionProvider actionProvider ;
//        actionProvider.setShareIntent();
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        return shareIntent;
        return null;
    }

}
