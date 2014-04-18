package changhong_Practice.image_object;

/**
 * Created by changhong on 14-3-28.
 */
public class Aulbum {
    public int containNumber = 1;
    public String bucket_id;
    public Image firstImage = null;

    public Aulbum(int containNumber, String bucket_id, Image firstImage) {
        this.containNumber = containNumber;
        this.bucket_id = bucket_id;
        this.firstImage = firstImage;
    }
}
