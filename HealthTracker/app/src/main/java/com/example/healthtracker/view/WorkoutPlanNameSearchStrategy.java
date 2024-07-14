package com.example.healthtracker.view;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class WorkoutPlanNameSearchStrategy implements Strategy {
    @Override
    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons) {
        container.removeAllViews();
        for (Button button: listOfButtons) {
            container.addView(button);
        }
        for (Button button: listOfButtons) {
            String buttonText = button.getText().toString();
            if ((!(buttonText.startsWith(query))) && (!query.contains("author:"))) {
                container.removeView(button);
            }
        }
    }
}
