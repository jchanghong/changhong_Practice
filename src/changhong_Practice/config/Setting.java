package changhong_Practice.config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by 蒋长宏 on 2014/4/11 0011 9:18
 * at jiangchanghong163@163.com
 */
public class Setting {
    private Setting() {
    }

    public static final String KEY_bud_ID = "mykeybudid";

    public static final void put(Context context, String key, Set<String> values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    public static final Set<String> get(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, null);
    }

}
