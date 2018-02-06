package org.zaregoto.apl.lasttimecounter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.model.ListableUnit;
import org.zaregoto.apl.lasttimecounter.ui.InputItemDialogFragment;
import org.zaregoto.apl.lasttimecounter.ui.ItemHistoryDialogFragment;
import org.zaregoto.apl.lasttimecounter.ui.RemoveItemAlertDialogFragment;

import java.util.ArrayList;

public abstract class ItemAction {

    enum TYPE_ACTION {
    }

    private String name;
    private String detail;

    private TYPE_ACTION typeAction;
    private Object args;

    private ItemAction() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public abstract boolean process(Context context, ListableUnit item);


    public static ArrayList<ItemAction> getInitialActions(Context context) {

        ItemAction action;
        ArrayList<ItemAction> actions = new ArrayList();

        action = new ItemAction() {
            @Override
            public boolean process(Context context, ListableUnit item) {

                if (item instanceof Item) {
                    InputItemDialogFragment inputItemDialog = InputItemDialogFragment.newInstance((Item) item, InputItemDialogFragment.INPUT_ITEM_DIALOG_MODE.REDO_MODE);
                    Activity activity = (Activity) context;
                    FragmentManager fm = activity.getFragmentManager();

                    inputItemDialog.show(fm, "");
                }

                return true;
            }
        };
        action.name = context.getString(R.string.redo_item_label);
        action.detail = context.getString(R.string.redo_item_detail_label);
        actions.add(action);


        action = new ItemAction() {
            @Override
            public boolean process(Context context, ListableUnit item) {

                if (item instanceof Item) {
                    ItemHistoryDialogFragment histroyDlg = ItemHistoryDialogFragment.newInstance((Item) item);
                    Activity activity = (Activity) context;
                    FragmentManager fm = activity.getFragmentManager();

                    histroyDlg.show(fm, "");
                }

                return true;
            }
        };
        //action.name = "履歴";
        //action.detail = "実行履歴表示";
        action.name = context.getString(R.string.history_label);
        action.detail = context.getString(R.string.history_detail_label);
        actions.add(action);

        action = new ItemAction() {
            @Override
            public boolean process(final Context context, final ListableUnit item) {

                RemoveItemAlertDialogFragment alertDlg = RemoveItemAlertDialogFragment.newInstance((Item) item, "title", "remove?");
                Activity activity = (Activity) context;
                FragmentManager fm = activity.getFragmentManager();

                alertDlg.show(fm, "");

                return true;
            }
        };
        //action.name = "削除";
        //action.detail = "データを削除します";
        action.name = context.getString(R.string.delete_label);
        action.detail = context.getString(R.string.delete_detail_label);
        actions.add(action);

        return actions;
    }
}
