package org.zaregoto.apl.lasttimecounter.model;

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
    private String label;

    private ItemType(String label) {
        this.label = label;
    }

    public ItemType(int id, String section, String filename, String label) {
        this.typeId = id;
        this.section = section;
        this.filename = filename;
        this.label = label;
    }

    private ItemType(Parcel in) {
        this.typeId = in.readInt();
        this.section = in.readString();
        this.filename = in.readString();
        this.label = in.readString();
    }

    public static ItemType createItemType(Context context, int typeId) {

        ItemType ret;

        ret = ItemStore.getItemType(context, typeId);
        return ret;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getFilename() {
        return filename;
    }


    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        out.writeString(label);
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
