package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputAlarmDialogFragment extends DialogFragment {

    private InputAlarmDialogListener mInputDialogListener;
    private final String TAG = "InputAlarmDialogFragment";

    public static InputAlarmDialogFragment newInstance() {

        InputAlarmDialogFragment instance = new InputAlarmDialogFragment();

        Bundle args = new Bundle();
        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_input_alarm_dialog, null);

        builder.setView(content);

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mInputDialogListener && context instanceof  InputAlarmDialogListener) {
            mInputDialogListener = (InputAlarmDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mInputDialogListener) {
            mInputDialogListener = null;
        }
        super.onDetach();
    }


    public interface InputAlarmDialogListener {
        void addItem(ItemUnit item);
        void updateItem(ItemUnit item);
    }
}
