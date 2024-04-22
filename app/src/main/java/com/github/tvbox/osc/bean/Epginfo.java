package com.github.tvbox.osc.bean;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Epginfo {

    public Date startdateTime;
    public Date enddateTime;
    public int datestart;
    public int dateend;
    public String title;
    public String originStart;
    public String originEnd;
    public String start;
    public String end;
    public int index;
    public Date epgDate;
    public String currentEpgDate = null;
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Epginfo(Date Date, String title, Date date, String start, String end, int pos) {
        this(date, title, date, start, end, pos, 0);
    }
    public Epginfo(Date Date, String title, Date date, String start, String end, int pos, int epgType) {
        if (epgType == 1){
            start = start.substring(8,10)+":"+start.substring(10,12);
            end = end.substring(8,10)+":"+end.substring(10,12);
        }
        epgDate = Date;
        currentEpgDate = timeFormat.format(epgDate);
        this.title = title;
        originStart = start;
        originEnd = end;
        index = pos;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat userSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        userSimpleDateFormat.setTimeZone(TimeZone.getDefault());
        startdateTime = userSimpleDateFormat.parse(simpleDateFormat.format(date) + " " + start + ":00 GMT+8:00", new ParsePosition(0));
        enddateTime = userSimpleDateFormat.parse(simpleDateFormat.format(date) + " " + end + ":00 GMT+8:00", new ParsePosition(0));
        SimpleDateFormat zoneFormat = new SimpleDateFormat("HH:mm");
        this.start = zoneFormat.format(startdateTime);
        this.end = zoneFormat.format(enddateTime);
        datestart = Integer.parseInt(this.start.replace(":", ""));
        dateend = Integer.parseInt(this.end.replace(":", ""));
    }

    @Override
    public String toString() {
        return "Epginfo{" +
                "startdateTime=" + startdateTime +
                ", enddateTime=" + enddateTime +
                ", datestart=" + datestart +
                ", dateend=" + dateend +
                ", title='" + title + '\'' +
                ", originStart='" + originStart + '\'' +
                ", originEnd='" + originEnd + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", index=" + index +
                ", epgDate=" + epgDate +
                ", currentEpgDate='" + currentEpgDate +
                '}';
    }
}