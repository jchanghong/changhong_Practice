package changhong_Practice.image_object;


import android.os.Parcel;
import android.os.Parcelable;
import changhong_Practice.uitl.MyTime;

import java.util.Date;

/**
 * Created by 蒋长宏 on 2014/4/2 0002.
 * at jiangchanghong163@163.com
 */
public class TimeImage extends Image implements Comparable<TimeImage>

        , Parcelable {
    public long mmakeTime = 0;

    public TimeImage(long id, String displayTitle, String path,
                     String bucket_id, long mmakeTime) {
        super(id, displayTitle, path, bucket_id);
        this.mmakeTime = mmakeTime;
    }

    public String getMmakeTime() {
        Date date = new Date(mmakeTime);
        MyTime myTime1 = new MyTime(date.getYear() + 1900,
                date.getMonth() + 1, date.getDay());
        return myTime1.toString();
    }

    public void setMmakeTime(long mmakeTime) {
        this.mmakeTime = mmakeTime;
    }

    @Override
    public int compareTo(TimeImage another) {
        if (mmakeTime == another.mmakeTime) return 0;
        return mmakeTime > another.mmakeTime ? -1 : 2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);

        dest.writeString(this.path);
        dest.writeLong(this.mmakeTime);
    }


    public static final Parcelable.Creator<TimeImage> CREATOR
            = new Parcelable.Creator<TimeImage>() {
        public TimeImage createFromParcel(Parcel in) {
            return new TimeImage(in);
        }

        public TimeImage[] newArray(int size) {
            return new TimeImage[size];
        }
    };

    private TimeImage(Parcel in) {
       id=in.readLong();
        path = in.readString();
        mmakeTime = in.readLong();
    }
}
