package changhong_Practice.ui.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.config.Setting;
import changhong_Practice.ui.BaseActivity;
import changhong_Practice.ui.HomeActivity;
import changhong_Practice.ui.SettingActivity;
import changhong_Practice.uitl.FileUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by 蒋长宏 on 2014/4/11 0011 10:31
 * at jiangchanghong163@163.com
 */
public class SetfilesActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    BaseAdapter baseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.setting_files);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        new task().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fileseting, menu);
//        MenuItem menuItem = menu.findItem(R.id.save);
//        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
    return     super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.save) {
            SetfilesAdpter setfilesAdpter = (SetfilesAdpter) listView.getAdapter();
            Setting.put(Constants.context, Setting.KEY_bud_ID, setfilesAdpter.getSelectPATH());
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("set", true);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, SettingActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        SetfilesAdpter setfilesAdpter = (SetfilesAdpter) baseAdapter;
//        setfilesAdpter.clicked(setfilesAdpter.getItem(position).toString());
    }

    class task extends AsyncTask<Void, Void, Map<String, List<String>>> {
        @Override
        protected void onPostExecute(Map<String, List<String>> stringListMap) {
            super.onPostExecute(stringListMap);
            baseAdapter = new SetfilesAdpter(stringListMap);
            listView.setAdapter(baseAdapter);
        }

        @Override
        protected Map<String, List<String>> doInBackground(Void... params) {
            return FileUtil.initallfile(FileUtil.init_All_path(Constants.context));
        }

    }

}
