package com.example.re;

public class CourseRequest {
    private String department;
    private String course_type;
    private String subject_type;
    private String course_name;
    private String course_code;
    private String professor;
    private String section;
    private String capacity;
    private String english;
    private String evaluation;
    private String mutual;
    private String prev_course_code;
    private String au;
    private String lecture_type;
    private String alternative_retake;
    private String edu4q;

    public CourseRequest(String department, String course_type, String subject_type, String course_name,
                         String course_code, String professor, String section, String capacity,
                         String english, String evaluation, String mutual, String prev_course_code,
                         String au, String lecture_type, String alternative_retake, String edu4q) {
        this.department = department;
        this.course_type = course_type;
        this.subject_type = subject_type;
        this.course_name = course_name;
        this.course_code = course_code;
        this.professor = professor;
        this.section = section;
        this.capacity = capacity;
        this.english = english;
        this.evaluation = evaluation;
        this.mutual = mutual;
        this.prev_course_code = prev_course_code;
        this.au = au;
        this.lecture_type = lecture_type;
        this.alternative_retake = alternative_retake;
        this.edu4q = edu4q;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getCourseType() { return course_type; }
    public void setCourseType(String course_type) { this.course_type = course_type; }
    public String getSubjectType() { return subject_type; }
    public void setSubjectType(String subject_type) { this.subject_type = subject_type; }
    public String getCourseName() { return course_name; }
    public void setCourseName(String course_name) { this.course_name = course_name; }
    public String getCourseCode() { return course_code; }
    public void setCourseCode(String course_code) { this.course_code = course_code; }
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }
    public String getEnglish() { return english; }
    public void setEnglish(String english) { this.english = english; }
    public String getEvaluation() { return evaluation; }
    public void setEvaluation(String evaluation) { this.evaluation = evaluation; }
    public String getMutual() { return mutual; }
    public void setMutual(String mutual) { this.mutual = mutual; }
    public String getPrevCourseCode() { return prev_course_code; }
    public void setPrevCourseCode(String prev_course_code) { this.prev_course_code = prev_course_code; }
    public String getAu() { return au; }
    public void setAu(String au) { this.au = au; }
    public String getLectureType() { return lecture_type; }
    public void setLectureType(String lecture_type) { this.lecture_type = lecture_type; }
    public String getAlternativeRetake() { return alternative_retake; }
    public void setAlternativeRetake(String alternative_retake) { this.alternative_retake = alternative_retake; }
    public String getEdu4q() { return edu4q; }
    public void setEdu4q(String edu4q) { this.edu4q = edu4q; }
}