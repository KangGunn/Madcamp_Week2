package com.example.re;

import java.util.List;

public class PreferencesResponse {
    private List<String> candidateTimetables;

    public PreferencesResponse(List<String> candidateTimetables) {
        this.candidateTimetables = candidateTimetables;
    }

    // Getters and Setters
    public List<String> getCandidateTimetables() {
        return candidateTimetables;
    }

    public void setCandidateTimetables(List<String> candidateTimetables) {
        this.candidateTimetables = candidateTimetables;
    }
}
