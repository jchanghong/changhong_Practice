package changhong_Practice.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.core.DisplayImageOptions;
import changhong_Practice.image_object.TimeImage;
import changhong_Practice.image_operate.Delete;
import changhong_Practice.uitl.Setwallpapper;
import uk.co.senab.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by changhong on 14-3-28.
 */
public class SingleImagePagerActivity extends BaseActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    ArrayList<TimeImage> images;
    PagerAdapter pagerAdapter;
    ShareActionProvider shareActionProvider;
    DisplayImageOptions options;
    private PhotoViewAttacher mAttacher;
    ViewPager pager;
   public static Handler handler;
    int currentposition = 0;
    public ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("等待...");
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.ac_image_pager);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        currentposition = bundle.getInt(Constants.Extra.IMAGE_POSITION, 0);
        images = bundle.getParcelableArrayList("images");
        if (savedInstanceState != null) {
            currentposition = savedInstanceState.getInt(STATE_POSITION);
        }
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentposition = position;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                Uri uri = Uri.fromFile(new File(images.get(currentposition).path));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                if (shareActionProvider != null)
                    shareActionProvider.setShareIntent(shareIntent);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter = new SingleAdpter(getSupportFragmentManager(), images);
        pager.setAdapter(pagerAdapter);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        progressDialog.show();
                        break;
                    case 2:
                        progressDialog.dismiss();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pager.setCurrentItem(currentposition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu, menu);
        MenuItem actionItem = menu.findItem(R.id.share);
        MenuItem crop = menu.findItem(R.id.crop);
        MenuItemCompat.setShowAsAction(crop, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setShowAsAction(actionItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItem menuItem = menu.findItem(R.id.delete);
        shareActionProvider = new ShareActionProvider(this);
        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setActionProvider(actionItem, shareActionProvider);
        shareActionProvider
                .setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(new File(images.get(currentposition).path));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareActionProvider.setShareIntent(shareIntent);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                delete();
                return true;
            case R.id.share:
                shares();
                return true;
            case R.id.crop:
                Intent intent = new Intent(Constants.context, CropActivity.class);
                intent.putExtra("image", images.get(currentposition));
                startActivity(intent);

                return true;
            case R.id.setwallpapper:
                Setwallpapper.setwallpapper(images.get(currentposition).path, this);
                return true;
            case android.R.id.home:
                startActivity(new Intent(this, ImageAlubumGirdActivity.class));
            default:
                return false;
        }
    }

    private void shares() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(new File(images.get(currentposition).path));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareActionProvider.setShareIntent(shareIntent);
//       Toast.makeText(getApplicationContext(),
//                "已经分享了，小孩", Toast.LENGTH_SHORT).show();

    }

    private void delete() {
//        Delete.delete(pagerAdapter,handler,timei,pager.getCurrentItem());
        new DleteTask().execute();
//        Toast.makeText(getApplicationContext(),
//                "已经删除了哦，亲", Toast.LENGTH_SHORT).show();
    }

    class DleteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Delete.delete(pagerAdapter, images, currentposition);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            pagerAdapter.notifyDataSetChanged();
            if (images.size() == 0) {
                Intent intent = new Intent(Constants.context, HomeActivity.class);
                intent.putExtra("set", true);
                startActivity(intent);

            } else if (currentposition >= 1) {
                pager.setCurrentItem(currentposition - 1);

            } else {
                pager.setCurrentItem(currentposition);
            }
        }
    }

}