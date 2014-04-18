package changhong_Practice.image_operate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by 蒋长宏 on 2014/4/2 0002.
 * at jiangchanghong163@163.com
 */
public class Threadpool {
    private static Executor executor = Executors.newFixedThreadPool(2);

    public static final void sub(Runnable runnable) {
        try {

            executor.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
