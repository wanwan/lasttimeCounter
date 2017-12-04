package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<LastTimeItem> {


    public ItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ItemAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ItemAdapter(Context context, int resource, LastTimeItem[] objects) {
        super(context, resource, objects);
    }

    public ItemAdapter(Context context, int resource, int textViewResourceId, LastTimeItem[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ItemAdapter(Context context, int resource, List<LastTimeItem> objects) {
        super(context, resource, objects);
    }

    public ItemAdapter(Context context, int resource, int textViewResourceId, List<LastTimeItem> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
