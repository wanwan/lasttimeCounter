package org.zaregoto.apl.lasttimecounter;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Item implements Comparable<Item>, Parcelable {

    public static int DEFAULT_TYPE_ID = 1;

    private int id;
    private String name;
    private String detail;
    private ItemType type;
    private Date createtime;
    private Date lastTime;


    private Item() {
        id = -1;
    }

    public Item(int id, String name, String detail, ItemType type, Date createtime, Date lastTime) {
        this();
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.createtime = createtime;
        this.lastTime = lastTime;
    }

    public Item(String name, String detail, ItemType type, Date createtime, Date lastTime) {
        this();
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.createtime = createtime;
        this.lastTime = lastTime;
    }

    public Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        detail = in.readString();
        type = in.readParcelable(Item.class.getClassLoader());
        createtime = (Date) in.readSerializable();
        lastTime = (Date) in.readSerializable();
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
