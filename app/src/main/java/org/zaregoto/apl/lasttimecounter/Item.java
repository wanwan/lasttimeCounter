package org.zaregoto.apl.lasttimecounter;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Date;

public class Item implements Comparable<Item> {

    private int id;
    private String name;
    private ArrayList<Date> times;
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

    public ArrayList<Date> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Date> times) {
        this.times = times;
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
        return 0;
    }
}
