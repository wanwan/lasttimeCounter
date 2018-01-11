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
import org.zaregoto.apl.lasttimecounter.ItemAction;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.model.Alarm;
import org.zaregoto.apl.lasttimecounter.model.Item;

import java.util.ArrayList;

public class ItemActionDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    private static final String ARGS_ITEM_ID = "ARGS_ITEM_ID";

    private InputAlarmDialogListener mInputDialogListener;
    private final String TAG = "ItemActionDialogFragment";
    private Alarm alarm;

    private View content;
    private InputAlarmDialogListener mDialogListener;

    private ListAdapter adapter;

    private Item selectedItem;

    public static ItemActionDialogFragment newInstance(Item item) {

        ItemActionDialogFragment instance = new ItemActionDialogFragment();

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
        content = inflater.inflate(R.layout.fragment_item_action_dialog, null);


        ListView lv = content.findViewById(R.id.actions);
        if (null != lv) {
            ArrayList<ItemAction> actions = ItemAction.getInitialActions(getContext());
            adapter = new ActionAdapter(getContext(), 0, actions);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        }

        builder.setView(content);

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

        ItemAction action = (ItemAction) adapter.getItem(pos);
        if (null != action) {
            if (action.process(getContext(), selectedItem)) {
                dismiss();
            }
        }
    }

    public interface InputAlarmDialogListener {
        void setAlarm(Alarm alarm);
    }


}
