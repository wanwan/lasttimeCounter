package org.zaregoto.apl.lasttimecounter;

import java.util.ArrayList;
import java.util.Date;

public class ItemHistory {

    private ArrayList<Date> history;

    private ItemHistory() {
    }

    public static ItemHistory getHistoryFromItem(Item item) {
        return new ItemHistory();
    }
}
