package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActionAdapter extends ArrayAdapter<ItemAction> {

    private LayoutInflater mInflater;

    public ActionAdapter(Context context, int resource, ArrayList<ItemAction> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemAction action = getItem(position);
        if (action instanceof ItemAction) {

            convertView = mInflater.inflate(R.layout.adapter_list_item_action, parent, false);

            TextView tv = convertView.findViewById(R.id.title);
            tv.setText(action.getName());

            tv = convertView.findViewById(R.id.detail);
            tv.setText(action.getDetail());
        }

        return convertView;
    }
}
