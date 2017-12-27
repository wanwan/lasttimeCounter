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

public class ItemAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;


    public ItemAdapter(Context context, int resource, ArrayList<Item> objects) {

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
            tv.setText(spf.format(item.getLastTime()));

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
}
