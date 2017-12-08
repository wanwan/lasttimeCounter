package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<LastTimeItem> {

    private LayoutInflater mInflater;


    public ItemAdapter(Context context, int resource, List<LastTimeItem> objects) {

        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_list_item_card, parent, false);
        }

        LastTimeItem item = getItem(position);

        TextView tv = (TextView) convertView.findViewById(R.id.title);
        tv.setText(item.getName());

        tv = (TextView) convertView.findViewById(R.id.sub);
        tv.setText(item.getDetail());

        ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
        iv.setImageDrawable(item.getImage());


        return convertView;
    }
}
