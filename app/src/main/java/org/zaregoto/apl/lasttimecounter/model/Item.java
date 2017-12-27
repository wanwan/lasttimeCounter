package org.zaregoto.apl.lasttimecounter.model;

import android.os.Parcel;
import android.os.Parcelable;

public interface Item extends Parcelable {
    String getName();

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel parcel, int i);
}
