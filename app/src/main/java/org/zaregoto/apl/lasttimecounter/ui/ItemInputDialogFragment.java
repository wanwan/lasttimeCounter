package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import org.zaregoto.apl.lasttimecounter.Item;
import org.zaregoto.apl.lasttimecounter.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.util.Date;

public class ItemInputDialogFragment extends DialogFragment {

    private int DEFAULT_TYPE_ID = 1;

    private InputDialogListener mInputDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Item item = null;
        if (null != getArguments()) {
            item = getArguments().getParcelable(MainActivity.ARGS_ITEM_ID);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_item_input_dialog, null);

        builder.setView(content);
        if (null != item) {
            EditText name = content.findViewById(R.id.name);
            name.setText(item.getName());
            EditText detail = content.findViewById(R.id.detail);
            detail.setText(item.getDetail());
        }

        final Item finalItem = item;
        builder.setMessage(R.string.fragment_item_input_dialog_name)
                .setNegativeButton(R.string.fragment_item_input_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (null == finalItem) {
                            EditText _name = content.findViewById(R.id.name);
                            EditText _detail = content.findViewById(R.id.detail);
                            String name = (_name != null) ? _name.getText().toString() : "";
                            String detail = (_detail != null) ? _detail.getText().toString() : "";
                            Date now = new Date();

                            ItemType type = ItemType.createItemType(getActivity(), DEFAULT_TYPE_ID);

                            Item _item = new Item(name, detail, type, now, now);
                            mInputDialogListener.addItem(_item);
                        }
                        else {
                            EditText _name = content.findViewById(R.id.name);
                            EditText _detail = content.findViewById(R.id.detail);
                            String name = (_name != null) ? _name.getText().toString() : "";
                            String detail = (_detail != null) ? _detail.getText().toString() : "";
                            Date now = new Date();

                            ItemType type = ItemType.createItemType(getActivity(), DEFAULT_TYPE_ID);

                            finalItem.setName(name);
                            finalItem.setDetail(detail);

                            mInputDialogListener.updateItem(finalItem);

                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mInputDialogListener && context instanceof  InputDialogListener) {
            mInputDialogListener = (InputDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mInputDialogListener) {
            mInputDialogListener = null;
        }
        super.onDetach();
    }

    public interface InputDialogListener {
        void addItem(Item item);
        void updateItem(Item item);
    }
}
