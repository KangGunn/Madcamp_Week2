package com.example.re;

public class Course {
    private final String courseName;
    private final String section;
    private final String time;
    private final String location;

    public Course(String courseName, String section, String time, String location) {
        this.courseName = courseName;
        this.section = section;
        this.time = time;
        this.location = location;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSection() {
        return section;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }
}
