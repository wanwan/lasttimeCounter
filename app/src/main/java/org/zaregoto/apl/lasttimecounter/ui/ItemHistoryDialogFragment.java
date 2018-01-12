package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.zaregoto.apl.lasttimecounter.*;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.Item;

import java.util.ArrayList;

public class ItemHistoryDialogFragment extends DialogFragment {

    private static final String ARGS_ITEM_ID = "ARGS_ITEM_ID";

    private ItemHistoryDialogListener mmListener;
    private final String TAG = "ItemHistoryDialogFragment";

    private View content;

    private HistoryAdapter adapter;
    private Item selectedItem;

    public static ItemHistoryDialogFragment newInstance(Item item) {

        ItemHistoryDialogFragment instance = new ItemHistoryDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM_ID, item);
        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (null != getArguments()) {
            Bundle bundle = getArguments();
            selectedItem = bundle.getParcelable(ARGS_ITEM_ID);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = inflater.inflate(R.layout.fragment_item_history_dialog, null);

        ListView lv = content.findViewById(R.id.histories);
        if (null != lv) {
            ArrayList<ItemHistory> histories = ItemStore.getHistoriesFromItem(getActivity(), selectedItem);
            adapter = new HistoryAdapter(getContext(), 0, histories);
            lv.setAdapter(adapter);
        }

        builder.setView(content);

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mmListener && context instanceof ItemHistoryDialogListener) {
            mmListener = (ItemHistoryDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mmListener) {
            mmListener = null;
        }
        super.onDetach();
    }


    public interface ItemHistoryDialogListener {
        void historySelected();
    }


}
