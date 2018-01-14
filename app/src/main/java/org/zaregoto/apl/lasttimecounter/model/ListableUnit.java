package org.zaregoto.apl.lasttimecounter.model;

import android.os.Parcel;
import android.os.Parcelable;

public interface ListableUnit extends Parcelable {

    enum SORT_TYPE {
        SORT_TYPE_NEWER_TO_OLD,
        SORT_TYPE_OLDER_TO_NEW,
        SORT_TYPE_NEARLEST_ALARM,
    }

    String getName();

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel parcel, int i);
}
