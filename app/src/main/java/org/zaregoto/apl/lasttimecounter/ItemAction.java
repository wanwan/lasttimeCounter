package org.zaregoto.apl.lasttimecounter;

import android.content.Context;

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

    public abstract boolean process(Context context);


    public static ArrayList<ItemAction> getInitialActions(Context context) {

        ItemAction action;
        ArrayList<ItemAction> actions = new ArrayList();

        // TODO: 文字列を resources に移動させる
        action = new ItemAction() {
            @Override
            public boolean process(Context context) {
                return true;
            }
        };
        action.name = "履歴";
        action.detail = "実行履歴表示";
        actions.add(action);

        action = new ItemAction() {
            @Override
            public boolean process(Context context) {
                return true;
            }
        };
        action.name = "削除";
        action.detail = "データを削除します";
        actions.add(action);

        return actions;
    }
}
