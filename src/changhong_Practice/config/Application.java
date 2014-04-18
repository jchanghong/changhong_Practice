
package changhong_Practice.config;

import android.content.Context;
import changhong_Practice.image_loader.core.ImageLoader;
import changhong_Practice.image_loader.core.ImageLoaderConfiguration;
import changhong_Practice.image_loader.core.assist.QueueProcessingType;

/**
 * Created by 蒋长宏 on 2014/4/1 0001.
 * at jiangchanghong163@163.com
 */
public class Application extends android.app.Application {

	public void onCreate() {
		super.onCreate();
        Constants.context = getApplicationContext();
        initImageLoader(Constants.context);
	}

	public static void initImageLoader(Context context) {

        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .smallWight(Viewsize.getSmallImageSize(context).width).
                samllHeigh(Viewsize.getSmallImageSize(context).heigth)
                .bigWight(Viewsize.getbigImageSize(context).width)
                .bigHeigh(Viewsize.getbigImageSize(context).heigth)
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }
}