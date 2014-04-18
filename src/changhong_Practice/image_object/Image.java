package changhong_Practice.image_object;

/**
 * Created by changhong on 14-3-28.
 */
public class Image {
    public String displayTitle;
    public String path = null;
    public String bucket_id;
    public long id=-1;
    public Image(long id,String displayTitle, String path, String bucket_id) {
        this.displayTitle = displayTitle;
        this.id=id;
        this.path = path;
        this.bucket_id = bucket_id;
    }

    public Image() {
    }
}
