package org.zaregoto.apl.lasttimecounter;

import java.util.ArrayList;
import java.util.Date;

public class LastTimeItem implements Comparable<LastTimeItem> {

    private String name;
    private ArrayList<Date> times;
    private String detail;


    private Date lastTime;

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
