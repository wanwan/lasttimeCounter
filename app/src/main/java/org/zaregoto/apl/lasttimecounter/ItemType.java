package org.zaregoto.apl.lasttimecounter;

import android.graphics.drawable.Drawable;

public class ItemType {

    private int id;
    private Drawable image;


    private ItemType() {
    }

    public ItemType(int id, Drawable image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public Drawable getImage() {
        return image;
    }
}
