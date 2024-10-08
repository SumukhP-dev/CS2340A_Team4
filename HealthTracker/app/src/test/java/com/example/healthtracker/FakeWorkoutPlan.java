package com.example.healthtracker;

public class FakeWorkoutPlan {
    private String title;
    private String name;
    private String notes;
    private String time;
    private String expectedCalories;
    private String sets;
    private String reps;

    public FakeWorkoutPlan(String title, String name, String notes,
                           String time, String calories, String sets, String reps) {
        this.title = title;
        this.name = name;
        this.notes = notes;
        this.time = time;
        this.expectedCalories = calories;
        this.sets = sets;
        this.reps = reps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExpectedCalories() {
        return expectedCalories;
    }

    public void setExpectedCalories(String expectedCalories) {
        this.expectedCalories = expectedCalories;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }
}
