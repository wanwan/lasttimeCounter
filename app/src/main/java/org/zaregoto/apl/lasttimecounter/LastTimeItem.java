package org.zaregoto.apl.lasttimecounter;

import android.graphics.drawable.Drawable;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

public class LastTimeItem implements Comparable<LastTimeItem> {

    private int id;
    private String name;
    private ArrayList<Date> times;
    private String detail;
    private Drawable image;
    private Date createtime;
    private Date lastTime;


    private LastTimeItem() {
        id = -1;
    }

    public LastTimeItem(int id, String name, String detail, Drawable image, Date createtime, Date lastTime) {
        this();
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.image = image;
        this.createtime = createtime;
        this.lastTime = lastTime;
    }

    public LastTimeItem(String name, String detail, Drawable image, Date createtime, Date lastTime) {
        this();
        this.name = name;
        this.detail = detail;
        this.image = image;
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

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
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
    public int compareTo(LastTimeItem lastTimeItem) {
        return 0;
    }
}
