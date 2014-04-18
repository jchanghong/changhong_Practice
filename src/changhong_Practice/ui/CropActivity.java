package changhong_Practice.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.utils.StorageUtils;
import changhong_Practice.image_object.TimeImage;
import com.edmodo.cropper.CropImageView;

import java.io.File;

/**
 * Created by lenovo on 14-4-16.
 */

public class CropActivity extends BaseActivity {

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final String PATH = "PATH";
    private  CropImageView cropImageView;
    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    private String path;
    TimeImage timeImage;
    Bitmap croppedImage;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);

    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_crop);
        timeImage = getIntent().getParcelableExtra("image");
        path = timeImage.path;
        Bundle bundle =new Bundle();
        bundle.putString(PATH, path);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize components of the app
        cropImageView  = (CropImageView) findViewById(R.id.cropimageview);
        cropImageView.setImageResource(path);


        // Sets initial aspect ratio to 10/10, for demonstration purposes
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);



        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu_corp, menu);
//        MenuItem actionItem = menu.findItem(R.id.save);
//        MenuItemCompat.setShowAsAction(actionItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                return true;

            case android.R.id.home:
                startActivity(new Intent(this, SingleImagePagerActivity.class));
                finish();
            default:
                return false;
        }
    }

    private void save() {
        croppedImage = cropImageView.getCroppedImage();
        File f = StorageUtils.getCacheDirectory(Constants.context);
        changhong_Practice.uitl.Savebitmap.Save(croppedImage, f.getAbsolutePath());
        finish();
    }

}