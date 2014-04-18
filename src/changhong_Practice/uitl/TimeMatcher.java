package changhong_Practice.uitl;

/**
 * Created by 蒋长宏 on 2014/4/2 0002.
 * at jiangchanghong163@163.com
 */
public class TimeMatcher {

    public static boolean match(String value, String keyword) {
        MyTime myTime = new MyTime(value);
        MyTime myTime1 = new MyTime(keyword);
        if (myTime.getYear() == myTime1.getYear()
                && myTime.getMouth() == myTime1.getMouth()) {
            return true;
        }
        return false;
    }


}
