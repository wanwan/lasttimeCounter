package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;

import java.io.IOException;
import java.io.InputStream;

public class ItemType {

    private int typeId;
    private Drawable image;


    private ItemType() {
    }

    public ItemType(int id, Drawable image) {
        this.typeId = id;
        this.image = image;
    }

    public static ItemType getItemType(Context context, int typeId) {

        Drawable drawable;
        ItemType ret = null;

        String filename = ItemStore.getItemTypeFileName(context, typeId);
        if (null != filename && filename.length() > 0) {
            try {
                InputStream is = context.getAssets().open("typeicons" + "/" + filename);
                drawable = Drawable.createFromStream(is, filename);
                if (null != drawable) {
                    ret = new ItemType(typeId, drawable);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public int getTypeId() {
        return typeId;
    }

    public Drawable getImage() {
        return image;
    }
}
