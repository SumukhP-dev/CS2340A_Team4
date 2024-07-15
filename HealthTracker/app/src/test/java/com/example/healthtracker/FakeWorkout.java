package com.example.healthtracker;

public class FakeWorkout {
    private String title;
    private String workoutName;
    private String additionalNotes;
    private int caloriesBurned;
    private int sets;
    private int reps;

    public FakeWorkout(String title, String workoutName, String notes, int calories, int sets, int reps) {
        this.title = title;
        this.workoutName = workoutName;
        this.additionalNotes = notes;
        this.caloriesBurned = calories;
        this.sets = sets;
        this.reps = reps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
    public Boolean check(){
        if (title.equals("") || workoutName.equals("") || caloriesBurned == 0 || sets == 0 || reps == 0) {
            return Boolean.FALSE;
        }
        else{
            return Boolean.TRUE;
        }
    }

    public int getCaloriesBurned()  {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
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
