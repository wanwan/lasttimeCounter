package org.zaregoto.apl.lasttimecounter.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.zaregoto.apl.lasttimecounter.ItemHistory;

import java.util.ArrayList;
import java.util.Date;

public class Item implements Comparable<Item>, Parcelable, ListableUnit {

    public static int DEFAULT_TYPE_ID = 1;

    private int id;
    private String name;
    private String detail;
    private ItemType type;
    private Date createtime;
    private Date lastTime;

    private Alarm alarm;

    private Item() {
        id = -1;
    }

    public Item(int id, String name, String detail, ItemType type, Date lastTime, Date createtime, Alarm alarm) {
        this();
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.lastTime = lastTime;
        this.createtime = createtime;

        this.alarm = alarm;
    }

    public Item(String name, String detail, ItemType type, Date lastTime, Date createtime, Alarm alarm) {
        this();
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.lastTime = lastTime;
        this.createtime = createtime;

        this.alarm = alarm;
    }

    public Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        detail = in.readString();
        type = in.readParcelable(Item.class.getClassLoader());
        lastTime = (Date) in.readSerializable();
        createtime = (Date) in.readSerializable();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }


    @Override
    public int compareTo(Item lastTimeItem) {
        return this.lastTime.compareTo(lastTimeItem.getLastTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(detail);
        out.writeParcelable(type, 0);
        out.writeSerializable(createtime);
        out.writeSerializable(lastTime);
    }


    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
