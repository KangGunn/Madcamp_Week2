package com.example.re;

import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("course_name")
    private final String courseName;
    @SerializedName("section")
    private final String section;
    @SerializedName("lecture_time")
    private final String lectureTime;
    @SerializedName("lecture_room")
    private final String lectureRoom;

    public Course(String courseName, String section, String lectureTime, String lectureRoom) {
        this.courseName = courseName;
        this.section = section;
        this.lectureTime = lectureTime;
        this.lectureRoom = lectureRoom;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSection() {
        return section;
    }

    public String getTime() {
        return lectureTime;
    }

    public String getLocation() {
        return lectureRoom;
    }
    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", section='" + section + '\'' +
                ", time='" + lectureTime + '\'' +
                ", location='" + lectureRoom + '\'' +
                '}';
    }
}
