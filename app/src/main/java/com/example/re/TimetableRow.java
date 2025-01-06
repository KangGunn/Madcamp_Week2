package com.example.re;

public class TimetableRow {
    private final String time;
    private final String monday;
    private final String tuesday;
    private final String wednesday;
    private final String thursday;
    private final String friday;

    public TimetableRow(String time, String monday, String tuesday, String wednesday, String thursday, String friday) {
        this.time = time;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
    }
    public String getTime() {
        return time;
    }
    public String getMonday() {
        return monday;
    }
    public String getTuesday() {
        return tuesday;
    }
    public String getWednesday() {
        return wednesday;
    }
    public String getThursday() {
        return thursday;
    }
    public String getFriday() {
        return friday;
    }
}