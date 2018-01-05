package org.zaregoto.apl.lasttimecounter.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import org.zaregoto.apl.lasttimecounter.R;

public class Alarm implements Parcelable {

    public enum ALARM_TYPE {
        ALARM_TYPE_NONE(0, 0, 0),
        ALARM_TYPE_DAY_BY_DAY(1, 1, 1),
        ALARM_TYPE_1WEEK(2, 7, 2),
        ALARM_TYPE_2WEEK(3, 14, 3),
        ALARM_TYPE_1MONTH(4, 30, 4),
        ALARM_TYPE_2MONTH(5, 60, 5),
        ALARM_TYPE_3MONTH(6, 90, 6),
        ALARM_TYPE_6MONTH(7, 180, 7),
        ALARM_TYPE_1YEAR(8, 365, 8),
        ALARM_TYPE_SET_SPECIFIC_DAY(9, null, 9),;

        private final int typeId;
        private final Integer defaultDurationDays;
        private final int strIdx;

        ALARM_TYPE(int id, Integer days, int strIdx) {
            this.typeId = id;
            this.defaultDurationDays = days;
            this.strIdx = strIdx;
        }

        public int getInt() {
            return this.typeId;
        }

        public static ALARM_TYPE getAlarmTypeByTypeId(int typeId) {

            ALARM_TYPE ret;

            if (typeId == ALARM_TYPE_NONE.getInt()) {
                ret = ALARM_TYPE_NONE;
            }
            else if (typeId == ALARM_TYPE_DAY_BY_DAY.getInt()) {
                ret = ALARM_TYPE_DAY_BY_DAY;
            }
            else if (typeId == ALARM_TYPE_1WEEK.getInt()) {
                ret = ALARM_TYPE_1WEEK;
            }
            else if (typeId == ALARM_TYPE_2WEEK.getInt()) {
                ret = ALARM_TYPE_2WEEK;
            }
            else if (typeId == ALARM_TYPE_1MONTH.getInt()) {
                ret = ALARM_TYPE_1MONTH;
            }
            else if (typeId == ALARM_TYPE_2MONTH.getInt()) {
                ret = ALARM_TYPE_2MONTH;
            }
            else if (typeId == ALARM_TYPE_3MONTH.getInt()) {
                ret = ALARM_TYPE_3MONTH;
            }
            else if (typeId == ALARM_TYPE_6MONTH.getInt()) {
                ret = ALARM_TYPE_6MONTH;
            }
            else if (typeId == ALARM_TYPE_1YEAR.getInt()) {
                ret = ALARM_TYPE_1YEAR;
            }
            else if (typeId == ALARM_TYPE_SET_SPECIFIC_DAY.getInt()) {
                ret = ALARM_TYPE_SET_SPECIFIC_DAY;
            }
            else {
                ret = null;
            }
            return ret;
        }

        public int getTypeId() {
            return typeId;
        }

        public int getStrIdx() {
            return strIdx;
        }

        public Integer getDefaultDurationDays() {
            return defaultDurationDays;
        }
    }

    private ALARM_TYPE type;
    private Integer days;

    private Alarm() {
    }

    public Alarm (ALARM_TYPE type) {
        this.type = type;
        this.days = null;
    }

    public Alarm (ALARM_TYPE type, int days) {
        this.type = type;
        this.days = days;
    }

    public Alarm(int _type, int days) {
        this.type = ALARM_TYPE.getAlarmTypeByTypeId(_type);
        this.days = days;
    }


    public Alarm(Parcel in) {
        int _type = in.readInt();
        days = (Integer) in.readValue(Integer.class.getClassLoader());
        type = ALARM_TYPE.getAlarmTypeByTypeId(_type);
    }

    public ALARM_TYPE getType() {
        return type;
    }

    public void setType(ALARM_TYPE type) {
        this.type = type;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getAlarmLabel(Context context) {
        String str = "";
        String[] array;
        if (null != type) {
            array = context.getResources().getStringArray(R.array.alarm_type_array);
            if (null != array && type.getStrIdx() < array.length) {
                str = array[type.getStrIdx()];
            }
        }

        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(type.getInt());
        out.writeValue(days);
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
