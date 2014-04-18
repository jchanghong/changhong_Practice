package changhong_Practice.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import changhong_Practice.changhong.R;
import changhong_Practice.image_operate.Threadpool;

/**
 * Created by 蒋长宏 on 2014/4/1 0001.
 * at jiangchanghong163@163.com
 */
public class WelcomeActivity extends Activity {
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                startActivity(new Intent(getApplicationContext(),
                        HomeActivity.class));
                finish();
                return false;
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                AlubumUtil.init_All_aulbum(Constants.context);
                handler.sendEmptyMessage(1);
            }
        };
        Threadpool.sub(runnable);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_welcome);
    }
}
