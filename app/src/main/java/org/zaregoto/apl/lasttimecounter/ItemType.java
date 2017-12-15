package org.zaregoto.apl.lasttimecounter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;

import java.io.IOException;
import java.io.InputStream;


public class ItemType implements Parcelable {

    private int typeId;
    private String section;
    private String filename;

    private ItemType() {
    }

    public ItemType(int id, String section, String filename) {
        this.typeId = id;
        this.section = section;
        this.filename = filename;
    }

    private ItemType(Parcel in) {
        typeId = in.readInt();
        section = in.readString();
        filename = in.readString();
    }

    public static ItemType createItemType(Context context, int typeId) {

        ItemType ret;

        ret = ItemStore.getItemType(context, typeId);
        return ret;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getFilename() {
        return filename;
    }

    public String getSection() {
        return section;
    }

    public Drawable getAsDrawableImage(Context context) throws IOException {

        Drawable drawable = null;
        AssetManager am;
        InputStream is;

        if (null != filename && filename.length() > 0 && null != section) {

            am = context.getAssets();
            is = am.open("typeicons" + "/" + section + "/" + filename);
            if (null != is) {
                drawable = Drawable.createFromStream(is, null);
            }
        }

        return drawable;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(typeId);
        out.writeString(section);
        out.writeString(filename);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public ItemType createFromParcel(Parcel in) {
            return new ItemType(in);
        }

        public ItemType[] newArray(int size) {
            return new ItemType[size];
        }
    };

}
