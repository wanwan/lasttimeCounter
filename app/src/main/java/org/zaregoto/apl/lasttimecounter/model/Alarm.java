package org.zaregoto.apl.lasttimecounter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable {

    public enum ALARM_TYPE {
        ALARM_TYPE_NONE(0),
        ALARM_TYPE_DAY_BY_DAY(1),
        ALARM_TYPE_1WEEK(2),
        ALARM_TYPE_2WEEK(3),
        ALARM_TYPE_1MONTH(4),
        ALARM_TYPE_2MONTH(5),
        ALARM_TYPE_3MONTH(6),
        ALARM_TYPE_6MONTH(7),
        ALARM_TYPE_1YEAR(8),
        ALARM_TYPE_SET_SPECIFIC_DAY(9),;

        private final int id;

        ALARM_TYPE(int id) {
            this.id = id;
        }

        public int getInt() {
            return this.id;
        }
    }

    private ALARM_TYPE type;
    private int days;

    private Alarm() {
    }

    public Alarm (ALARM_TYPE type, int days) {
        this.type = type;
        this.days = days;
    }

    public Alarm(int _type, int days) {

        if (_type == ALARM_TYPE.ALARM_TYPE_NONE.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_NONE;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_DAY_BY_DAY.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_DAY_BY_DAY;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_1WEEK.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_1WEEK;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_2WEEK.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_2WEEK;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_1MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_1MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_2MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_2MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_3MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_3MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_6MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_6MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_1YEAR.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_1YEAR;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_SET_SPECIFIC_DAY.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_SET_SPECIFIC_DAY;
        }

        this.days = days;
    }


    public Alarm(Parcel in) {
        int _type = in.readInt();
        days = in.readInt();
        if (_type == ALARM_TYPE.ALARM_TYPE_NONE.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_NONE;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_DAY_BY_DAY.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_DAY_BY_DAY;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_1WEEK.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_1WEEK;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_2WEEK.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_2WEEK;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_1MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_1MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_2MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_2MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_3MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_3MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_6MONTH.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_6MONTH;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_1YEAR.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_1YEAR;
        }
        else if (_type == ALARM_TYPE.ALARM_TYPE_SET_SPECIFIC_DAY.getInt()) {
            type = ALARM_TYPE.ALARM_TYPE_SET_SPECIFIC_DAY;
        }
    }

    public ALARM_TYPE getType() {
        return type;
    }

    public void setType(ALARM_TYPE type) {
        this.type = type;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(type.getInt());
        out.writeInt(days);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };


}
