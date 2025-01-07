package com.example.re;

import java.sql.Time;
import java.util.List;

public class PreferencesResponse {
    private List<Timetable> candidateTimetables;
    public List<Timetable> getCandidateTimetables() { return candidateTimetables; }
    public void setCandidateTimetables(List<Timetable> candidateTimetables) { this.candidateTimetables = candidateTimetables; }

    public static class Timetable {
        private List<Course> courses;
        public List<Course> getCourses() { return courses; }
        public void setCourses(List<Course> courses) { this.courses = courses; }

        public static class Course {
            private String course_name;
            private String course_code;
            private String section;
            private String lecture_room;
            private String lecture_time;

            public String getCourse_name() {
                return course_name;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public String getCourse_code() {
                return course_code;
            }

            public void setCourse_code(String course_code) {
                this.course_code = course_code;
            }

            public String getSection() {
                return section;
            }

            public void setSection(String section) {
                this.section = section;
            }

            public String getLecture_room() {
                return lecture_room;
            }

            public void setLecture_room(String lecture_room) {
                this.lecture_room = lecture_room;
            }

            public String getLecture_time() {
                return lecture_time;
            }

            public void setLecture_time(String lecture_time) {
                this.lecture_time = lecture_time;
            }
        }
    }
}
