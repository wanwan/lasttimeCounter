package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.model.ItemHeader;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemListAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;

    public ItemListAdapter(Context context, int resource, ArrayList<Item> objects) {

        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemUnit item;
        ItemHeader header;
        Item _item = getItem(position);
        if (_item instanceof ItemUnit) {
            item = (ItemUnit) _item;
            convertView = mInflater.inflate(R.layout.adapter_list_item_unit, parent, false);

            TextView tv = convertView.findViewById(R.id.title);
            tv.setText(item.getName());

            tv = convertView.findViewById(R.id.detail);
            tv.setText(item.getDetail());

            tv = convertView.findViewById(R.id.lastupdate);
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            int days = diffDateBetweenToday(item.getLastTime());
            String _str = spf.format(item.getLastTime()) + "(" + days + ")";
            tv.setText(_str);



            ImageView iv = convertView.findViewById(R.id.icon);
            if (null != item && null != item.getType()) {
                try {
                    Drawable drawable = item.getType().getAsDrawableImage(this.getContext());
                    iv.setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (_item instanceof ItemHeader) {
            header = (ItemHeader) _item;
            convertView = mInflater.inflate(R.layout.adapter_list_item_header, parent, false);

            TextView tv = convertView.findViewById(R.id.header_title);
            if (null != tv) {
                tv.setText(header.getName());
            }
        }

        return convertView;
    }

    private int diffDateBetweenToday(Date _lastTime) {

        Date _now = new Date();
        long now;
        long lastTime;
        long _ret;
        int ret = 0;

        if (null != _lastTime) {
            now = _now.getTime();
            lastTime = _lastTime.getTime();
            _ret = (now - lastTime) / (24*60*60*1000);
            ret = (int) _ret;
        }

        return ret;
    }
}
