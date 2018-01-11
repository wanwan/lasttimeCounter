package org.zaregoto.apl.lasttimecounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;
import org.zaregoto.apl.lasttimecounter.ui.AlertDialogFragment;

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

    public abstract boolean process(Context context, Item item);


    public static ArrayList<ItemAction> getInitialActions(Context context) {

        ItemAction action;
        ArrayList<ItemAction> actions = new ArrayList();

        // TODO: 文字列を resources に移動させる
        action = new ItemAction() {
            @Override
            public boolean process(Context context, Item item) {
                return true;
            }
        };
        action.name = "履歴";
        action.detail = "実行履歴表示";
        actions.add(action);

        action = new ItemAction() {
            @Override
            public boolean process(final Context context, final Item item) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("title");
//                builder.setMessage("message");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // OK button pressed
//                        ItemStore.deleteData(context, item);
//                    }
//                });
//                builder.setNegativeButton("Cancel", null);
//
//                AlertDialog dialog = builder.create();
//                dialog.show();

                AlertDialogFragment alertDlg = AlertDialogFragment.newInstance((ItemUnit) item, "title", "remove?");
                Activity activity = (Activity) context;
                FragmentManager fm = activity.getFragmentManager();
                alertDlg.setAlertDialogListener(new AlertDialogFragment.AlertDialogListener() {
                    @Override
                    public void ok() {
                        ItemStore.deleteData(context, item);
                    }
                    @Override
                    public void cancel() {
                        // do nothing
                    }
                });
                alertDlg.show(fm, "");

                return true;
            }
        };
        action.name = "削除";
        action.detail = "データを削除します";
        actions.add(action);

        return actions;
    }
}
