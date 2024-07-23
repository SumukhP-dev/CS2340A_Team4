package com.example.healthtracker.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;

public class NewWorkoutPlan implements WorkoutPlan {
    private DataSnapshot userSnapshot;
    private Context context;
    private FragmentManager fragmentManager;
    private LinearLayout linearLayoutWorkoutPlanPopupScrollView;
    private int color;

    public NewWorkoutPlan(DataSnapshot userSnapshot, Context context,
                          FragmentManager fragmentManager,
                          LinearLayout linearLayoutWorkoutPlanPopupScrollView) {
        this.userSnapshot = userSnapshot;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.linearLayoutWorkoutPlanPopupScrollView = linearLayoutWorkoutPlanPopupScrollView;
        color = Color.BLUE;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public LinearLayout getLinearLayoutWorkoutPlanPopupScrollView() {
        return linearLayoutWorkoutPlanPopupScrollView;
    }

    public void setLinearLayoutWorkoutPlanPopupScrollView(LinearLayout linearLayoutWorkoutPlanPopupScrollView) {
        this.linearLayoutWorkoutPlanPopupScrollView = linearLayoutWorkoutPlanPopupScrollView;
    }

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
