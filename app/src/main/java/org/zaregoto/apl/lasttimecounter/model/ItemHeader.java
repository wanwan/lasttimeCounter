package org.zaregoto.apl.lasttimecounter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemHeader implements ListableUnit {

    private String name;

    private ItemHeader() {
    }

    public ItemHeader(String name) {
        this.name = name;
    }

    public ItemHeader(Parcel in) {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public ItemHeader createFromParcel(Parcel in) {
            return new ItemHeader(in);
        }

        public ItemHeader[] newArray(int size) {
            return new ItemHeader[size];
        }
    };
}
