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
import android.widget.ImageView;
import org.zaregoto.apl.lasttimecounter.Item;
import org.zaregoto.apl.lasttimecounter.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.util.Date;

public class ProgressDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_progress, null);

        builder.setView(content);

        builder.setMessage(R.string.fragment_progress_dialog_name);
        builder.setPositiveButton(R.string.fragment_progress_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
