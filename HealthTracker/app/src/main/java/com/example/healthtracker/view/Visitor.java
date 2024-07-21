package com.example.healthtracker.view;

public interface Visitor {
    public void visit(OldWorkoutPlan oldWorkoutPlan);
    public void visit(NewWorkoutPlan newWorkoutPlan);
}
