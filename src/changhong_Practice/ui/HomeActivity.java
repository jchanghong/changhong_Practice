package changhong_Practice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import changhong_Practice.changhong.R;
import changhong_Practice.image_object.Aulbum;
import changhong_Practice.image_operate.HomeLoader;

import java.util.List;

/**
 * Created by changhong on 14-3-28.
 */
public class HomeActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<List<Aulbum>> {
    GridView gridView;
    private  HomeAdapter homeAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_changhong_homegrid);
        gridView = (GridView) findViewById(R.id.homegrid);
        gridView.setOnItemClickListener(this);
       getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Aulbum aulbum = (Aulbum) homeAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(),
                ImageAlubumGirdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("bid", aulbum.bucket_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(getApplicationContext(),
                        SettingActivity.class));
                return true;
            case android.R.id.home:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public Loader<List<Aulbum>> onCreateLoader(int id, Bundle args) {
        return new HomeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Aulbum>> loader, List<Aulbum> data) {
        if (homeAdapter != null) {
            homeAdapter.setmAll(data);
            homeAdapter.notifyDataSetChanged();
            return;
        }
        homeAdapter = new HomeAdapter(data, this);
        gridView.setAdapter(homeAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Aulbum>> loader) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        boolean b = intent.getBooleanExtra("set", false);
        if (b) {
            getSupportLoaderManager().restartLoader(0, null, this);
        }
        super.onNewIntent(intent);
    }
}