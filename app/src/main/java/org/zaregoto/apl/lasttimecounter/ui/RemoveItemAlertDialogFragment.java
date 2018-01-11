package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.model.Item;


public class RemoveItemAlertDialogFragment extends DialogFragment {

    private static final String ARGS_ITEM_ID = "ARGS_ITEM";
    private static final String ARGS_TITLE_ID = "ARGS_TITLE";
    private static final String ARGS_MSG_ID = "ARGS_MSG";

    private AlertDialogListener mListener;
    private Item item;

    private View content;
    private String msg;
    private String title;

    public static RemoveItemAlertDialogFragment newInstance(Item item, String title, String msg) {

        RemoveItemAlertDialogFragment instance = new RemoveItemAlertDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM_ID, item);
        args.putString(ARGS_TITLE_ID, title);
        args.putString(ARGS_MSG_ID, msg);
        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (null != getArguments()) {
            Bundle bundle = getArguments();
            item = bundle.getParcelable(ARGS_ITEM_ID);
            title = bundle.getString(ARGS_TITLE_ID);
            msg = bundle.getString(ARGS_MSG_ID);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = inflater.inflate(R.layout.fragment_alert_dialog, null);

        TextView tv = content.findViewById(R.id.alertMsg);
        if (null != tv) {
            tv.setText(msg);
        }

        builder.setView(content);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (null != mListener) {
                    mListener.removeItemConfirm(item);
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (null != mListener) {
                    mListener.cancelRemoveItem(item);
                }
            }
        });

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mListener && context instanceof AlertDialogListener) {
            mListener = (AlertDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mListener) {
            mListener = null;
        }
        super.onDetach();
    }

    public interface AlertDialogListener {
        void removeItemConfirm(Item item);
        void cancelRemoveItem(Item item);
    }

    public void setAlertDialogListener(AlertDialogListener listener) {
        mListener = listener;
    }

}
