package com.example.votingapp.enums;

public enum PollStatus {
    TO_DO("To-do"), ON_GOING("On-going"), DONE("Done"), DELETED("Deleted");

    private String value;

    private PollStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // For showing status' value instead of the enum label.
    @Override
    public String toString() {
        return value;
    }
}
