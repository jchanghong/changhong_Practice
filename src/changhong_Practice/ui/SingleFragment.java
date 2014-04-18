package changhong_Practice.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.core.DisplayImageOptions;
import changhong_Practice.image_object.TimeImage;

import java.util.List;

/**
 * Created by 蒋长宏 on 2014/4/16 0016 8:52
 * at jiangchanghong163@163.com
 */
public class SingleFragment extends Fragment {
    private int curentposition = 0;
    private List<TimeImage> images = null;
    Activity context = null;
    DisplayImageOptions option;

    private SingleFragment() {
    }

    public static SingleFragment getInstance(List<TimeImage> images, int postion) {
        SingleFragment frament = new SingleFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList("images", (java.util.ArrayList<? extends android.os.Parcelable>) images);
        b.putInt("position", postion);
        frament.setArguments(b);
        return frament;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        images = b.getParcelableArrayList("images");
        curentposition = b.getInt("position");
        option = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
//			.displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_pager_image, container, false);
        assert view != null;
        ImageView imageview = (ImageView) view.findViewById(R.id.image);
        String p = images.get(curentposition).path;
        Constants.imageLoader.displayImage(p, imageview, option, true);
        return view;
    }

}
