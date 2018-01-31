package org.zaregoto.apl.lasttimecounter;

import org.zaregoto.apl.lasttimecounter.model.Item;

import java.util.ArrayList;
import java.util.Date;

public class ItemHistory {

    private Date history;
    private String detail;

    private ItemHistory() {
    }

    public ItemHistory(Date date, String _detail) {
        history = date;
        detail = _detail;
    }

    public Date getHistory() {
        return history;
    }

    public void setHistory(Date history) {
        this.history = history;
    }

    public String getDetail() {
        return detail;
    }

}
