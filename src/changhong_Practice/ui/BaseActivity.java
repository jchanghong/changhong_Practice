package changhong_Practice.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import changhong_Practice.image_loader.core.ImageLoader;
/**
 * Created by 蒋长宏 on 2014/4/1 0001.
 * at jiangchanghong163@163.com
 */
public abstract class BaseActivity extends ActionBarActivity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected android.support.v7.app.ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
    }
}
