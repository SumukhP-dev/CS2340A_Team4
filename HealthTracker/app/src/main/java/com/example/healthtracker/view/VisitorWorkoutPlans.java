package com.example.healthtracker.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;

import com.example.healthtracker.R;
import com.google.firebase.database.DataSnapshot;

public class VisitorWorkoutPlans implements Visitor {

    @Override
    public void visit(OldWorkoutPlan oldWorkoutPlan) {
        addDataToWorkoutPlansScrollView(oldWorkoutPlan.getUserSnapshot(),
                oldWorkoutPlan.getContext(),
                oldWorkoutPlan.getLinearLayoutWorkoutPlanPopupScrollView(),
                oldWorkoutPlan.getColor());
    }

    @Override
    public void visit(NewWorkoutPlan newWorkoutPlan) {
        addDataToWorkoutPlansScrollView(newWorkoutPlan.getUserSnapshot(),
                newWorkoutPlan.getContext(),
                newWorkoutPlan.getLinearLayoutWorkoutPlanPopupScrollView(),
                newWorkoutPlan.getColor());
    }


    public void addDataToWorkoutPlansScrollView(DataSnapshot userSnapshot, Context context,
                                    LinearLayout linearLayoutWorkoutPlanPopupScrollView,
                                                int color) {
            String name = userSnapshot.child("name")
                    .getValue(String.class);

            if ((name != null) && (context != null)) {
                Button workoutButton = new Button(context);
                workoutButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                workoutButton.setPadding(16, 16, 16, 16);
                workoutButton.setBackgroundResource(R.drawable.gray_rounded_corner);

                String buttonText = String.format("%s", name);
                workoutButton.setText(buttonText);

                workoutButton.setBackgroundColor(color);
                linearLayoutWorkoutPlanPopupScrollView.addView(workoutButton);
/*
                // Add some space between buttons
                View spacer = new View(context);
                spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        8)); // 8dp height
                linearLayoutWorkoutPlanPopupScrollView.addView(spacer);
                8
 */
            }
    }
}
