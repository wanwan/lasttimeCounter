package org.zaregoto.apl.lasttimecounter;

import org.zaregoto.apl.lasttimecounter.model.ItemUnit;

import java.util.ArrayList;
import java.util.Date;

public class ItemHistory {

    private ArrayList<Date> history;

    private ItemHistory() {
    }

    public static ItemHistory getHistoryFromItem(ItemUnit item) {
        return new ItemHistory();
    }
}
