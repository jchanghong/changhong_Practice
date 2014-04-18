package changhong_Practice.image_operate;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import changhong_Practice.config.Constants;
import changhong_Practice.config.Setting;
import changhong_Practice.image_object.Aulbum;
import changhong_Practice.image_object.Image;
import changhong_Practice.ui.HomeActivity;
import changhong_Practice.uitl.FileUtil;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by 蒋长宏 on 2014/4/14 0014 13:44
 * at jiangchanghong163@163.com
 */
public class HomeLoader extends AsyncTaskLoader<List<Aulbum>> {
    private  Cursor getcursor(Context context) {
        ContentResolver contentResolver =
                context.getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Constants.PROJECTION, null, null, null);
        return cursor;
    }
    final ForceLoadContentObserver mObserver;
    List<Aulbum> mlist;
    Cursor mCursor;
    Cursor current;

    /* Runs on a worker thread */
    @Override
    public List<Aulbum> loadInBackground() {
        List<Aulbum> re=null;
        Cursor cursor = getcursor(Constants.context);
        if (cursor != null) {
            current=cursor;
            cursor.registerContentObserver(mObserver);
            // Ensure the cursor window is filled
            Set<String> budid= Setting.get(Constants.context, Setting.KEY_bud_ID);
            Log.i("changhong", "init begin");
            Map<String, Aulbum> hashMap = new HashMap<String, Aulbum>();
            while (cursor.moveToNext()) {
                String bid = cursor.getString(Constants.INDEX_BUCKET_ID);
                String path = cursor.getString(Constants.INDEX_DATA);
                String direc= FileUtil.getdirectory(path);
                if (budid==null)
                {
                }
                else {
                    if (!budid.contains(direc))continue;
                }
                if (hashMap.keySet().contains(bid)) {
                    hashMap.get(bid).containNumber++;
                    continue;
                }
                long id = cursor.getLong(Constants.INDEX_ID);
                String title=cursor.getString(Constants.INDEX_CAPTION);
                Image image = new Image(id,title,
                        path, bid);
                Aulbum aulbum = new Aulbum(1, bid, image);
                hashMap.put(bid, aulbum);
            }
            re = new ArrayList<Aulbum>();
            for (Map.Entry<String, Aulbum> entry : hashMap.entrySet()) {
                re.add(entry.getValue());
            }
            cursor.close();
            Log.i("changhong", "init success");
        }
        return re;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(List<Aulbum> list) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (list != null) {
                list.clear();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = current;
        mlist = list;

        if (isStarted()) {
            super.deliverResult(list);
        }

        if (oldCursor != null && oldCursor != current && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Creates an empty unspecified CursorLoader.  You must follow this with
     * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc
     * to specify the query to perform.
     */
    public HomeLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
    }


    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     *
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mlist);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Aulbum> cursor) {
        if (cursor != null ) {
            cursor.clear();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
        if (mlist!=null)
            mlist.clear();

    }

}
