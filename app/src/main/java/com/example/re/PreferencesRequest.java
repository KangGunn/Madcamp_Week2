package com.example.re;

import java.util.List;

public class PreferencesRequest {
    private List<String> dislikedTimes;
    private List<String> mandatoryCourses;
    private List<String> dislikedCourses;
    private int minCredits;
    private int maxCredits;
    private List<PreferencesActivity.MajorCourse> majorCourses;
    private int generalCourses;

    public PreferencesRequest(List<String> dislikedTimes, List<String> mandatoryCourses, List<String> dislikedCourses,
                              int minCredits, int maxCredits, List<PreferencesActivity.MajorCourse> majorCourses, int generalCourses) {
        this.dislikedTimes = dislikedTimes;
        this.mandatoryCourses = mandatoryCourses;
        this.dislikedCourses = dislikedCourses;
        this.minCredits = minCredits;
        this.maxCredits = maxCredits;
        this.majorCourses = majorCourses;
        this.generalCourses = generalCourses;
    }

    // Getters and Setters
    public List<String> getDislikedTimes() {
        return dislikedTimes;
    }

    public void setDislikedTimes(List<String> dislikedTimes) {
        this.dislikedTimes = dislikedTimes;
    }

    public List<String> getMandatoryCourses() {
        return mandatoryCourses;
    }

    public void setMandatoryCourses(List<String> mandatoryCourses) {
        this.mandatoryCourses = mandatoryCourses;
    }

    public List<String> getDislikedCourses() {
        return dislikedCourses;
    }

    public void setDislikedCourses(List<String> dislikedCourses) {
        this.dislikedCourses = dislikedCourses;
    }

    public int getMinCredits() {
        return minCredits;
    }

    public void setMinCredits(int minCredits) {
        this.minCredits = minCredits;
    }

    public int getMaxCredits() {
        return maxCredits;
    }

    public void setMaxCredits(int maxCredits) {
        this.maxCredits = maxCredits;
    }

    public List<PreferencesActivity.MajorCourse> getMajorCourses() {
        return majorCourses;
    }

    public void setMajorCourses(List<PreferencesActivity.MajorCourse> majorCourses) {
        this.majorCourses = majorCourses;
    }

    public int getGeneralCourses() {
        return generalCourses;
    }

    public void setGeneralCourses(int generalCourses) {
        this.generalCourses = generalCourses;
    }
}
