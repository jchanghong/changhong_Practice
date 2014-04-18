package changhong_Practice.ui.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import changhong_Practice.changhong.R;
import changhong_Practice.config.Constants;
import changhong_Practice.config.Setting;

import java.util.*;

/**
 * Created by 蒋长宏 on 2014/4/11 0011 10:31
 * at jiangchanghong163@163.com
 */
public class SetfilesAdpter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    final Set<String> orage = new HashSet<String>();
    final LinkedList<String> allitem;
    final LinkedList<Boolean> allitemBoolen;
    final SortedMap<String, Boolean> sortedMap;
    final Set<String> selectPATH = new HashSet<String>();
     Map<String,List<String>> allfiles;
    public Set<String> getSelectPATH() {
        return selectPATH;
    }

    public SetfilesAdpter(Map<String,List<String>> allfiles) {
        this.allfiles = allfiles;
        allitem = new LinkedList<String>();
        Set<String> temp= Setting.get(Constants.context,Setting.KEY_bud_ID);
        sortedMap = new TreeMap<String, Boolean>();
        allitemBoolen = new LinkedList<Boolean>();
        for (String s : allfiles.keySet()) {
            boolean b=false;
            if (temp != null && temp.contains(s)) {
                b = true;
            }
            allitemBoolen.add(b);
            sortedMap.put(s, b);
            allitem.add(s);
            orage.add(s);
        }

    }

    public void clicked(String item) {
//        if (orage.contains(item)) {
         boolean checked=sortedMap.get(item);
            List<String> list =allfiles.get(item);
            if (list.size() == 0) {
                return;
            } else {
                if (allitem.contains(list.get(0))) {
                    for (String s : list) {
                        sortedMap.remove(s);
                    }
                } else {
                    for (String s : list) {
                        sortedMap.put(s, checked);
                    }
                }
                syn();
                notifyDataSetChanged();
            }
//        }

    }

    private void syn() {
        allitem.clear();
        allitemBoolen.clear();
        for (Map.Entry<String, Boolean> en : sortedMap.entrySet()) {
            allitem.add(en.getKey());
            allitemBoolen.add(en.getValue());
        }
    }

    @Override
    public int getCount() {
        return allitem.size();
    }

    @Override
    public Object getItem(int position) {
        return allitem.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        viewholder h = null;
        if (view == null) {
            view = LayoutInflater.from(Constants.context).
                    inflate(R.layout.setting_files_item, null);
            assert view != null;
            h = new viewholder();
            h.imageview = (ImageView) view.findViewById(R.id.imageadd);
            h.text = (TextView) view.findViewById(R.id.textView);
            h.checkbox = (CheckBox) view.
                    findViewById(R.id.checkbox);
            h.imageview.setOnClickListener(this);
            h.checkbox.setOnCheckedChangeListener(this);
            view.setTag(h);
        } else {
            h = (viewholder) view.getTag();
        }
        String s = getItem(position).toString();
        h.imageview.setTag(s);
        h.checkbox.setTag(s);
        h.text.setText(s);
        h.checkbox.setChecked(allitemBoolen.get(position));
        if (isdirectory(s)) {
            h.imageview.setVisibility(View.VISIBLE);
        } else {
            h.imageview.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        String tag = buttonView.getTag().toString();
        if (isChecked) {
            selectPATH.add(tag);
            if (isdirectory(tag))
            selectPATH.addAll(allfiles.get(tag));
        } else {
            selectPATH.remove(tag);
            if (isdirectory(tag)) {
               selectPATH.removeAll(allfiles.get(tag));

            }
        }
        if (!isdirectory(tag)||tag.length()==17)

        {
            sortedMap.put(tag, isChecked);
            syn();
            return;
        }
            for (String s : allitem) {
                if (s.startsWith(tag)) {
                    sortedMap.put(s, isChecked);
                }
            }
        syn();
        notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.imageadd) {
            clicked(v.getTag().toString());
//        }
    }

    boolean isdirectory(String s) {
        return orage.contains(s);
    }
    class viewholder {
        public TextView text;
        public CheckBox checkbox;
        public ImageView imageview;
    }
}
