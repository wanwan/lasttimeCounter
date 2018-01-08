package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.zaregoto.apl.lasttimecounter.ActionAdapter;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.model.Alarm;

public class ItemActionDialogFragment extends DialogFragment {

    private InputAlarmDialogListener mInputDialogListener;
    private final String TAG = "ItemActionDialogFragment";
    private Alarm alarm;

    private View content;
    private InputAlarmDialogListener mDialogListener;

    public static ItemActionDialogFragment newInstance() {

        ItemActionDialogFragment instance = new ItemActionDialogFragment();

        Bundle args = new Bundle();
        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (null != getArguments()) {
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = inflater.inflate(R.layout.fragment_item_action_dialog, null);

        ListView lv = content.findViewById(R.id.actions);
        if (null != lv) {
            ListAdapter adapter = new ActionAdapter();
            lv.setAdapter();
        }

        builder.setView(content);
        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (null != mDialogListener) {
                    mDialogListener.setAlarm(alarm);
                }
                dismiss();
            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mInputDialogListener && context instanceof InputAlarmDialogListener) {
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

    // TODO: onAttach との整合の問題があるが, 上書きというルールであるとする. 通常は問題ないはずだが, 回転等の再作成を経る場合があるので良いやりかたではないはず
    public void setDialogListener(InputAlarmDialogListener listener) {
        this.mDialogListener = listener;
    }

    public interface InputAlarmDialogListener {
        void setAlarm(Alarm alarm);
    }


}
