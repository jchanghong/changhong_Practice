package changhong_Practice.image_operate;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import changhong_Practice.config.Constants;
import changhong_Practice.image_object.TimeImage;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蒋长宏 on 2014/4/10 0002.
 * at jiangchanghong163@163.com
 * 本定义只是换回结果不同
 */
public class TimeimageLoader extends AsyncTaskLoader<List<TimeImage>> {
    final ForceLoadContentObserver mObserver;
    Uri mUri;
    String[] mProjection;
    String mSelection;
    String[] mSelectionArgs;
    String mSortOrder;
    Cursor mCursor;
    Cursor mcurCursor;
    List<TimeImage> mtimeImages=null;
    /* Runs on a worker thread */
    @Override
    public List<TimeImage> loadInBackground() {
        Cursor cursor = getContext().getContentResolver().query(mUri, mProjection, mSelection,
                mSelectionArgs, mSortOrder);
        mcurCursor=cursor;
        List<TimeImage> timeImages=null;
        if (cursor != null) {
            // Ensure the cursor window is filled
//            cursor.getCount();
            timeImages = new ArrayList<TimeImage>();
            cursor.registerContentObserver(mObserver);
            while (cursor.moveToNext()) {
                String path = cursor.getString(Constants.INDEX_DATA);
                long id = cursor.getLong(Constants.INDEX_ID);
                long time = cursor.getLong(Constants.INDEX_DATE_TAKEN);
                TimeImage timeImage = new TimeImage(id,null, path, null, time);
                timeImage.id = id;
                timeImages.add(timeImage);
            }
        }
        return timeImages;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(List<TimeImage> timeImageList) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (mcurCursor != null) {
                mcurCursor.close();
            }
            if (timeImageList != null) {
                timeImageList.clear();
            }
            return;
        }
        List<TimeImage> old=mtimeImages;
        mtimeImages=timeImageList;
        Cursor oldCursor = mCursor;
        mCursor = mcurCursor;

        if (isStarted()) {
            super.deliverResult(timeImageList);
        }
        if (old != null) {
            old.clear();
        }
        if (oldCursor != null && oldCursor != mcurCursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Creates an empty unspecified CursorLoader.  You must follow this with
     * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc
     * to specify the query to perform.
     */
    public TimeimageLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
    }

    /**
     * Creates a fully-specified CursorLoader.  See
     * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)
     * ContentResolver.query()} for documentation on the meaning of the
     * parameters.  These will be passed as-is to that call.
     */
    public TimeimageLoader(Context context, Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mUri = uri;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
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
        if (mCursor != null&&mtimeImages!=null) {
            deliverResult(mtimeImages);
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
    public void onCanceled(List<TimeImage> timeImageList) {
        if (mcurCursor != null && !mcurCursor.isClosed()) {
            mcurCursor.close();
        }
        if (timeImageList != null) {
            timeImageList.clear();
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
        if (mtimeImages != null) {
            mtimeImages.clear();
        }
        mtimeImages=null;
        mCursor = null;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public void setProjection(String[] projection) {
        mProjection = projection;
    }

    public String getSelection() {
        return mSelection;
    }

    public void setSelection(String selection) {
        mSelection = selection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        mSelectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(String sortOrder) {
        mSortOrder = sortOrder;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
//        writer.print(prefix); writer.print("mUri="); writer.println(mUri);
//        writer.print(prefix); writer.print("mProjection=");
//        writer.println(Arrays.toString(mProjection));
//        writer.print(prefix); writer.print("mSelection="); writer.println(mSelection);
//        writer.print(prefix); writer.print("mSelectionArgs=");
//        writer.println(Arrays.toString(mSelectionArgs));
//        writer.print(prefix); writer.print("mSortOrder="); writer.println(mSortOrder);
//        writer.print(prefix); writer.print("mCursor="); writer.println(mCursor);
//        writer.print(prefix); writer.print("mContentChanged=");
//        writer.println(mContentChanged);
    }
}
