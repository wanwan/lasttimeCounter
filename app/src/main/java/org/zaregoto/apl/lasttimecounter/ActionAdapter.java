package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import org.zaregoto.apl.lasttimecounter.model.ItemType;

import java.util.ArrayList;

public class ActionAdapter extends ArrayAdapter<ItemAction> {

    public ActionAdapter(Context context, int resource, ArrayList<ItemAction> objects) {
        super(context, resource, objects);
    }


}
