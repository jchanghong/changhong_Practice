package changhong_Practice.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.core.ImageLoader;
import changhong_Practice.image_object.Aulbum;
import changhong_Practice.image_object.Image;

import java.util.List;

/**
 * Created by changhong on 14-3-28.
 */
public class HomeAdapter extends BaseAdapter {

    List<Aulbum> mAll;

    public void setmAll(List<Aulbum> mAll) {
        this.mAll = mAll;
    }

    Activity mcontext;
    ImageLoader loader;
    public HomeAdapter(List<Aulbum> all,Activity context) {
        mAll = all;
        mcontext = context;
        loader = Constants.imageLoader;
    }

    @Override
    public int getCount() {
        return mAll.size();
    }

    @Override
    public Object getItem(int position) {
        return mAll.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = mcontext.getLayoutInflater().inflate(R.layout.changhong_homegrid_item,
                    parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            holder.title = (TextView) view.
                    findViewById(R.id.title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Image image = mAll.get(position).firstImage;
        int sum = mAll.get(position).containNumber;
        loader.displayImage(image.path,
                holder.imageView, null, false);
        holder.title.setText(image.displayTitle+"("+sum+")");
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView title;
    }
}
