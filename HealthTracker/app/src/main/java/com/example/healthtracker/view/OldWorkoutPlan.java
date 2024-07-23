package com.example.healthtracker.view;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;

public class OldWorkoutPlan implements WorkoutPlan {
    private DataSnapshot userSnapshot;
    private Context context;
    private FragmentManager fragmentManager;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

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
