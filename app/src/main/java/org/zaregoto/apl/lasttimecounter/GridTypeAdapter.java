package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.model.ItemHeader;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GridTypeAdapter extends ArrayAdapter<ItemType> {

    private LayoutInflater mInflater;
    private Context context;

    public GridTypeAdapter(Context context, int resource, ArrayList<ItemType> objects) {

        super(context, resource, objects);
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemType itemType = getItem(position);
        AssetManager am = context.getAssets();
        InputStream is;
        String filename;
        String section;
        Drawable drawable;

        convertView = mInflater.inflate(R.layout.item_type, null);
        if (null != convertView) {
            ImageView iconView = convertView.findViewById(R.id.type_icon);
            TextView labelView = convertView.findViewById(R.id.type_label);

            filename = itemType.getFilename();
            section = itemType.getSection();

            try {
                is = am.open("typeicons" + "/" + section + "/" + filename);
                if (null != is) {
                    drawable = Drawable.createFromStream(is, null);
                    View typeView = mInflater.inflate(R.layout.item_type, null);

                    iconView.setImageDrawable(drawable);
                    labelView.setText("aaaa");
                    //Log.d(TAG, "***** type icon width: height " + typeView.getWidth() + " " + typeView.getHeight());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

}
