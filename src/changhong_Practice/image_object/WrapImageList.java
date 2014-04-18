package changhong_Practice.image_object;

import changhong_Practice.uitl.MyTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蒋长宏 on 2014/4/2 0002.
 * at jiangchanghong163@163.com
 */
public class WrapImageList {
    public MyTime myTime;
    public List<String> mimages = new ArrayList<String>();

    public WrapImageList(MyTime myTime, List<String> mimages) {
        this.myTime = myTime;
        this.mimages = mimages;
    }
}
