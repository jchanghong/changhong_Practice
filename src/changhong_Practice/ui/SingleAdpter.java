package changhong_Practice.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import changhong_Practice.image_object.TimeImage;

import java.util.List;

/**
 * Created by 蒋长宏 on 2014/4/16 0016 9:12
 * at jiangchanghong163@163.com
 */
public class SingleAdpter extends FragmentPagerAdapter {
    List<TimeImage> images;
    public SingleAdpter(FragmentManager fm, List<TimeImage> images) {
        super(fm);
        this.images = images;
    }
    @Override
    public Fragment getItem(int position) {
        return SingleFragment.getInstance(images, position);
    }
    @Override
    public int getCount() {
        return images.size();
    }

}
