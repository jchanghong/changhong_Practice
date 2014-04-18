package changhong_Practice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.ui.setting.SetfilesActivity;

/**
 * Created by 蒋长宏 on 2014/4/1 0001.
 * at jiangchanghong163@163.com
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.ac_setting);
        Button button;
        button= (Button) findViewById(R.id.buttonmemory);
        button.setOnClickListener(this);
        button= (Button) findViewById(R.id.disccache);
        button.setOnClickListener(this);
        button= (Button) findViewById(R.id.files);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.more);
        button.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonmemory:
                Constants.imageLoader.clearMemoryCache();
                break;
            case R.id.disccache:
                Constants.imageLoader.clearDiscCache();
                break;
            case R.id.more:
                more();
                break;
            case R.id.files:
                setfiles();
                break;
        }
    }

    private void setfiles() {
        Intent intent = new Intent(this, SetfilesActivity.class);
        startActivity(intent);
    }

    private void more() {

    }
}