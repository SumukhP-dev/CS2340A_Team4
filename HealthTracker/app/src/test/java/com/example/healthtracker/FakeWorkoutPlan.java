package com.example.healthtracker;

public class FakeWorkoutPlan {
    private String title;
    private String name;
    private String notes;
    private String time;
    private int expectedCalories;
    private int sets;
    private int reps;

    public FakeWorkoutPlan(String title, String name, String notes, String time, int calories, int sets, int reps) {
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

    public int getExpectedCalories() {
        return expectedCalories;
    }

    public void setExpectedCalories(int expectedCalories) {
        this.expectedCalories = expectedCalories;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
