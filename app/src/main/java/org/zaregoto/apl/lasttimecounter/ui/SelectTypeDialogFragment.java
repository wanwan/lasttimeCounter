package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;
import org.zaregoto.apl.lasttimecounter.R;

public class SelectTypeDialogFragment extends DialogFragment {

    private SelectTypeDialogFragment.SelectTypeDialogListener mDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.8);
        int dialogHeight = (int) (metrics.heightPixels * 0.8);

        ItemUnit item = null;
        if (null != getArguments()) {
            item = getArguments().getParcelable(MainActivity.ARGS_ITEM_ID);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_select_type, null);

        builder.setView(content);

        GridLayout grid = content.findViewById(R.id.gridview);
        if (null != grid) {

        }

        builder.setMessage(R.string.fragment_type_select_dialog_name);
        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );

        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mDialogListener && context instanceof SelectTypeDialogListener) {
            mDialogListener = (SelectTypeDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mDialogListener) {
            mDialogListener = null;
        }
        super.onDetach();
    }

    public interface SelectTypeDialogListener {
    }

}
