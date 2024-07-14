package com.example.healthtracker.view;

import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class WorkoutPlanNameSearchStrategy implements Strategy {
    @Override
    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons) {
        for (Button button: listOfButtons) {
            String buttonText = button.getText().toString();
            String text = buttonText.substring(0, query.length());
            if (!(text.equals(query))) {
                container.removeView(button);
            }
        }
    }
}
