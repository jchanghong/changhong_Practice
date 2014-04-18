package changhong_Practice.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.core.ImageLoader;
import changhong_Practice.image_loader.core.listener.PauseOnScrollListener;
import changhong_Practice.image_object.TimeImage;
import changhong_Practice.image_operate.Delete;
import changhong_Practice.image_operate.TimeimageLoader;
import changhong_Practice.ui.mychtrol.StickyGridHeadersTimeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by changhong on 14-3-28.
 */
public class ImageAlubumGirdActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<List<TimeImage>> {
    StickyGridHeadersTimeAdapter mtimeAdapter;
   public static List<TimeImage> timeImageList = new ArrayList<TimeImage>();
    String bid = "";
    GridView mgridview;
    static final String[] PROJECTION = Constants.PROJECTION;
    static final String SELECTION = MediaStore.Images.ImageColumns.BUCKET_ID
            + " = " + "?";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.ac_sticky_grid);
        Bundle bundle = getIntent().getExtras();
        bid = bundle.getString("bid");
        mgridview = (GridView) findViewById(R.id.gridview);
        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ImageAlubumGirdActivity.this, SingleImagePagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("images", (ArrayList<? extends android.os.Parcelable>) timeImageList);
                bundle.putInt(Constants.Extra.IMAGE_POSITION, position);
                intent.putExtras(bundle);
                ImageAlubumGirdActivity.this.startActivity(intent);
            }
        });
        init();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {
        mgridview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mgridview.setFastScrollEnabled(true);
        multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
            Set<String> set = new HashSet<String>();
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (id<0)return;
                if (checked) set.add(timeImageList.get((int) id).path);
                else set.remove(timeImageList.get((int) id).path);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.images_actionmode_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        Delete.delete((BaseAdapter) mgridview.getAdapter(), timeImageList, set);
                        mode.finish();
                        break;
                    case R.id.share:
                        mode.finish();
                        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        ArrayList<Uri> uris = new ArrayList<Uri>();
                        for (String s : set) {
                            uris.add(Uri.fromFile(new File(s)));
                        }
                        shareIntent.setType("image/*");
                        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(shareIntent.createChooser(shareIntent, "选择分享的应用"));
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        };
        mgridview.setMultiChoiceModeListener(multiChoiceModeListener);
    }

    AbsListView.MultiChoiceModeListener multiChoiceModeListener;
    protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
    protected boolean pauseOnScroll = false;
    protected boolean pauseOnFling = true;

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
        pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        applyScrollListener();
    }

    private void applyScrollListener() {
        mgridview.setOnScrollListener(
                new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem pauseOnScrollItem = menu.findItem(R.id.item_pause_on_scroll);
        pauseOnScrollItem.setVisible(true);
        pauseOnScrollItem.setChecked(pauseOnScroll);

        MenuItem pauseOnFlingItem = menu.findItem(R.id.item_pause_on_fling);
        pauseOnFlingItem.setVisible(true);
        pauseOnFlingItem.setChecked(pauseOnFling);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.images_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_pause_on_scroll:
                pauseOnScroll = !pauseOnScroll;
                item.setChecked(pauseOnScroll);
                applyScrollListener();
                return true;
            case R.id.item_pause_on_fling:
                pauseOnFling = !pauseOnFling;
                item.setChecked(pauseOnFling);
                applyScrollListener();
                return true;
            case R.id.delete:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                    startActivity(new Intent(this, HomeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    @Override
    public Loader<List<TimeImage>> onCreateLoader(int id, Bundle args) {
        String[] str = new String[]{bid};
        TimeimageLoader timeimageLoader=new TimeimageLoader(Constants.context,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                PROJECTION, SELECTION, str, null);
        return timeimageLoader;
    }
    @Override
    public void onLoadFinished(Loader<List<TimeImage>> loader, List<TimeImage> data) {
        timeImageList=data;
        mtimeAdapter = new StickyGridHeadersTimeAdapter(Constants.context, data);
        mgridview.setAdapter(mtimeAdapter);
    }
    @Override
    public void onLoaderReset(Loader<List<TimeImage>> loader) {
        timeImageList = new ArrayList<TimeImage>();
        mtimeAdapter = new StickyGridHeadersTimeAdapter(Constants.context, timeImageList);
        mgridview.setAdapter(mtimeAdapter);
    }
}