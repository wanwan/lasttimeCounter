package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;


    public ItemAdapter(Context context, int resource, ArrayList<Item> objects) {

        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_list_item_card, parent, false);
        }

        Item item = getItem(position);

        TextView tv = convertView.findViewById(R.id.title);
        tv.setText(item.getName());

        tv = convertView.findViewById(R.id.detail);
        tv.setText(item.getDetail());

        ImageView iv = convertView.findViewById(R.id.icon);
        if (null != item && null != item.getType()) {
            try {
                Drawable drawable = item.getType().getAsDrawableImage(this.getContext());
                iv.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return convertView;
    }
}
