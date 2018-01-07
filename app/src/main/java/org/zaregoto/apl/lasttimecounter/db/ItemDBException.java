package org.zaregoto.apl.lasttimecounter.db;

import java.text.ParseException;

public class ItemDBException extends Exception {

    public ItemDBException() {
    }

    public ItemDBException(ParseException e) {
        super(e);
    }
}
