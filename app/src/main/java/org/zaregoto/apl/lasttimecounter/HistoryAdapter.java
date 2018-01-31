package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<ItemHistory> {

    private LayoutInflater mInflater;

    public HistoryAdapter(Context context, int resource, ArrayList<ItemHistory> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemHistory history = getItem(position);
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (history instanceof ItemHistory) {

            convertView = mInflater.inflate(R.layout.adapter_list_history_unit, parent, false);

            String _str = spf.format(history.getHistory());

            TextView tv = convertView.findViewById(R.id.do_date);
            tv.setText(_str);

            tv = convertView.findViewById(R.id.detail);
            tv.setText(history.getDetail());
        }

        return convertView;
    }
}
