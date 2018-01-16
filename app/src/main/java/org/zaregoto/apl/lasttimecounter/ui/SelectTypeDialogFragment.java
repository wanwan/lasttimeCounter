package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import org.zaregoto.apl.lasttimecounter.GridTypeAdapter;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.util.ArrayList;

public class SelectTypeDialogFragment extends DialogFragment {

    private static final String ARGS_CANCELABLE_ID = "cancelable";

    private final String TAG = "TypeDialogFragment";
    private SelectTypeDialogFragment.SelectTypeDialogListener mDialogListener;

    public static SelectTypeDialogFragment newInstance(boolean cancelable) {

        SelectTypeDialogFragment instance = new SelectTypeDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_CANCELABLE_ID, cancelable);

        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        boolean cancelable = false;

        Bundle args = getArguments();
        if (null != args) {
            cancelable = args.getBoolean(ARGS_CANCELABLE_ID);
        }

        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.8);
        int dialogHeight = (int) (metrics.heightPixels * 0.65);
        double iconViewRatio = dialogHeight / dialogWidth;
        ArrayList<ItemType> types = ItemStore.getAllItemTyps(getActivity());
        final GridTypeAdapter adapter = new GridTypeAdapter(getActivity(), android.R.layout.simple_list_item_1, types);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_select_type, null);

        builder.setView(content);

        GridView gridView = content.findViewById(R.id.gridview);
        if (null != gridView) {
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    ItemType type = adapter.getItem(pos);
                    Log.d(TAG, "ItemType clicked: " + type.getSection() + " " + type.getFilename());
                    if (null != mDialogListener) {
                        mDialogListener.selectType(type);
                    }
                    dismiss();
                }
            });

        }

        if (cancelable) {
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (null != mDialogListener) {
                        mDialogListener.selectType(null);
                    }
                }
            });
        }

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


    // TODO: onAttach との整合の問題があるが, 上書きというルールであるとする. 通常は問題ないはずだが, 回転等の再作成を経る場合があるので良いやりかたではないはず
    public void setDialogListener(SelectTypeDialogListener listener) {
        this.mDialogListener = listener;
    }


    public interface SelectTypeDialogListener {
        void selectType(ItemType type);
    }

}
