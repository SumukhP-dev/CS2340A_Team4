package com.example.healthtracker.view;

import com.google.firebase.database.DataSnapshot;

public class OldWorkoutPlan implements WorkoutPlan {
    private DataSnapshot userSnapshot;

    public OldWorkoutPlan(DataSnapshot userSnapshot) {
        this.userSnapshot = userSnapshot;
    }

    public DataSnapshot getUserSnapshot() {
        return userSnapshot;
    }

    public void setUserSnapshot(DataSnapshot userSnapshot) {
        this.userSnapshot = userSnapshot;
    }

    @Override
    public void completeAction(VisitorWorkoutPlans visitor) {
        visitor.visit(this);
    }
}
