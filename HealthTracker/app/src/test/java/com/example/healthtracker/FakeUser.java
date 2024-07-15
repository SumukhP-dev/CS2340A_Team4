package com.example.healthtracker;

import java.util.ArrayList;

public class FakeUser {
    private String username;
    private String password;
    private String name;
    private String gender;
    private int counter;
    private double height;
    private double weight;
    private ArrayList<FakeWorkout> workouts;
    private ArrayList<FakeWorkoutPlan> workoutPlans;



    public FakeUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.counter = 0;
        workouts = new ArrayList<FakeWorkout>();
        workoutPlans = new ArrayList<FakeWorkoutPlan>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter += 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Boolean addWorkout(FakeWorkout workout) {
        if (workout.check() == Boolean.TRUE) {
            workouts.add(workout);
            incrementCounter();
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }

    }

    public void addWorkoutPlan(FakeWorkoutPlan workoutPlan) {
        workoutPlans.add(workoutPlan);
    }

    public ArrayList<FakeWorkout> getWorkout(){ return workouts;}

    public ArrayList<FakeWorkoutPlan> getWorkoutPlans(){ return workoutPlans;}

}
