package com.example.healthtracker.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
                oldWorkoutPlan.getFragmentManager(),
                oldWorkoutPlan.getLinearLayoutWorkoutPlanPopupScrollView());
    }

    @Override
    public void visit(NewWorkoutPlan newWorkoutPlan) {
        addDataToWorkoutPlansScrollView(newWorkoutPlan.getUserSnapshot(),
                newWorkoutPlan.getContext(),
                newWorkoutPlan.getFragmentManager(),
                newWorkoutPlan.getLinearLayoutWorkoutPlanPopupScrollView());
    }


    public void addDataToWorkoutPlansScrollView(DataSnapshot userSnapshot, Context context,
                                    FragmentManager fragmentManager,
                                    LinearLayout linearLayoutWorkoutPlanPopupScrollView) {
        String userId = userSnapshot.getKey();


        for (DataSnapshot workoutSnapshot : userSnapshot.getChildren()) {
            String workoutId = workoutSnapshot.getKey();
            String cals = workoutSnapshot.child("expectedCalories")
                    .getValue(String.class);
            String name = workoutSnapshot.child("name")
                    .getValue(String.class);
            String notes = workoutSnapshot.child("notes")
                    .getValue(String.class);
            String reps = workoutSnapshot.child("reps")
                    .getValue(String.class);
            String sets = workoutSnapshot.child("sets")
                    .getValue(String.class);
            String time = workoutSnapshot.child("time")
                    .getValue(String.class);

            if ((name != null) && (context != null)) {
                Button workoutButton = new Button(context);
                workoutButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                workoutButton.setPadding(16, 16, 16, 16);
                workoutButton.setBackgroundResource(R.drawable.gray_rounded_corner);

                String buttonText = String.format("%s\t%s ", name, userId);
                workoutButton.setText(buttonText);

                workoutButton.setOnClickListener(v -> {
                    boolean check = false;
                    int color = 0;
                    if (workoutButton.getBackground()
                            .getClass().equals(GradientDrawable.class)) {
                        check = true;
                    } else {
                        ColorDrawable workoutButtonColorDrawable
                                = (ColorDrawable) workoutButton.getBackground();
                        color = workoutButtonColorDrawable.getColor();
                    }
                    if (check || color != Color.GREEN) {
                        WorkoutsIndividualFragment detailFragment
                                = new WorkoutsIndividualFragment();

                        // Create a Bundle to pass data to the new fragment
                        Bundle args = new Bundle();
                        args.putString("userId", userId);
                        args.putString("expectedCalories", cals);
                        args.putString("name", name);
                        args.putString("notes", notes);
                        args.putString("reps", reps);
                        args.putString("sets", sets);
                        args.putString("time", time);
                        detailFragment.setArguments(args);

                        // Perform the fragment transaction
                        //FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout2, detailFragment)
                                // Replace R.id.frameLayout2 with your container ID
                                .addToBackStack(null)
                                .commit();
                    }
                });

                linearLayoutWorkoutPlanPopupScrollView.addView(workoutButton);

                // Add some space between buttons
                View spacer = new View(context);
                spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        8)); // 8dp height
                linearLayoutWorkoutPlanPopupScrollView.addView(spacer);
            }
        }
    }
}
