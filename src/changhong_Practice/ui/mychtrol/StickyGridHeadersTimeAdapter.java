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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.image_loader.core.ImageLoader;
import changhong_Practice.image_object.TimeImage;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by changhong on 14-3-30.
 */
public class StickyGridHeadersTimeAdapter extends BaseAdapter implements
        StickyGridHeadersBaseAdapter {
    ImageLoader imageLoader;
    Context context;
    private int mHeaderResId;
    private LayoutInflater mInflater;
    private int mItemResId;
    private List<TimeImage> mItems;
    public int[] intsfoeheader;

    public StickyGridHeadersTimeAdapter(Context context, List<TimeImage> items
    ) {
        this.context = context;
        init(context, items);
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

//    @Override
//    public long getHeaderId(int position) {
//        Date date;
//        TimeImage item = (TimeImage) getItem(position);
//        String s = item.getMmakeTime();
//        MyTime myTime = new MyTime(s);
//        date = new Date(
//                myTime.getYear(), myTime.getMouth(), myTime.getDay());
//        return date.getTime();
//    }

    @Override
    public int getCountForHeader(int header) {
        return intsfoeheader[header];
    }

    @Override
    public int getNumHeaders() {
        return intsfoeheader.length;
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
        holder.textView.setText(section[position]);
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
        imageLoader = Constants.imageLoader;
        Map<String, Integer> mapforHeadnumber = new TreeMap<String, Integer>();
        for (TimeImage timeImage : mItems) {
            if (mapforHeadnumber.keySet().contains(timeImage.getMmakeTime())) {
                int num = mapforHeadnumber.get(timeImage.getMmakeTime());
                num++;
                mapforHeadnumber.put(timeImage.getMmakeTime(), num);
            } else {
                mapforHeadnumber.put(timeImage.getMmakeTime(), 1);
            }
        }
        section = new String[mapforHeadnumber.keySet().size()];
        intsfoeheader = new int[mapforHeadnumber.entrySet().size()];
        int i = 0;
        for (Map.Entry<String, Integer> integerEntry : mapforHeadnumber.entrySet()) {
            intsfoeheader[i] = integerEntry.getValue();
            section[i] = integerEntry.getKey();
            i++;
        }
    }

    public String[] section;

    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
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
}
