package org.zaregoto.apl.lasttimecounter;

import org.zaregoto.apl.lasttimecounter.model.Item;

import java.util.ArrayList;
import java.util.Date;

public class ItemHistory {

    private Date history;


    private ItemHistory() {
    }

    public ItemHistory(Date date) {
        history = date;
    }

    public Date getHistory() {
        return history;
    }

    public void setHistory(Date history) {
        this.history = history;
    }

}
