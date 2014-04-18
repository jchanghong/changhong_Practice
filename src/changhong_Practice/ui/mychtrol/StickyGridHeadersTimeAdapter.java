/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package changhong_Practice.ui.mychtrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.core.ImageLoader;
import changhong_Practice.image_object.TimeImage;
import changhong_Practice.ui.SingleImagePagerActivity;
import changhong_Practice.uitl.MyTime;

import java.util.*;

public class StickyGridHeadersTimeAdapter extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter, SectionIndexer {
    ImageLoader imageLoader;
    protected static final String TAG = StickyGridHeadersTimeAdapter.class.getSimpleName();
    Context context;
    private int mHeaderResId;

    private LayoutInflater mInflater;

    private int mItemResId;

    private List<TimeImage> mItems;

    public StickyGridHeadersTimeAdapter(Context context, List<TimeImage> items
    ) {
        this.context = context;
        init(context, items);
    }

    public StickyGridHeadersTimeAdapter(Context context, TimeImage[] items
    ) {
        this.context = context;
        init(context, Arrays.asList(items));
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public long getHeaderId(int position) {
        Date date;
        TimeImage item = (TimeImage) getItem(position);
        String s = item.getMmakeTime();
        MyTime myTime = new MyTime(s);
        date = new Date(
                myTime.getYear(), myTime.getMouth(), myTime.getDay());
        return date.getTime();
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);

            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.timetext);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        TimeImage item = (TimeImage) getItem(position);
        holder.textView.setText(item.getMmakeTime());
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String path = ((TimeImage) getItem(position)).path;
        imageLoader.displayImage(path, holder.imageView, null, false);
        return convertView;
    }

    private void init(Context context, List<TimeImage> items) {
        this.mItems = items;
        this.mHeaderResId = R.layout.sticky_grid_header;
        this.mItemResId = R.layout.item_sticky_grid_item;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        Set<String> set = new HashSet<String>();
        for (TimeImage timeImage : items) {
            set.add(timeImage.getMmakeTime());
        }
        section = new String[set.size()];
        int i = 0;
        for (String s : set) {
            section[i++] = s;
        }
    }

    String[] section;

    @Override
    public Object[] getSections() {
        return section;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        String time = section[sectionIndex];
        int i = 0;
        for (TimeImage timeImage : mItems) {
            if (timeImage.getMmakeTime().equals(time)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    protected class HeaderViewHolder {
        public TextView textView;
//        public ImageView share;
//        public ImageView delete;
//        public ImageView select;

    }

    protected class ViewHolder {
        public ImageView imageView;
    }

    public class onitemclicket implements AdapterView.OnItemClickListener {

        List<String> images;

        public void setImages(List<String> images) {
            this.images = images;
        }

        onitemclicket(List<String> images) {
            this.images = images;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, SingleImagePagerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(Constants.Extra.IMAGES, (ArrayList<String>) images);
            bundle.putInt(Constants.Extra.IMAGE_POSITION, position);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

}
