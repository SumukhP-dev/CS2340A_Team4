package com.example.healthtracker.view;

import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class WorkoutPlanAuthorSearchStrategy implements Strategy {
    @Override
    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons) {
        for (Button button: listOfButtons) {
            String buttonText = button.getText().toString();
            int indexOfText = buttonText.indexOf("/t");
            String text = buttonText.substring(indexOfText + 2);

            int indexOfQuery = query.indexOf("author:");
            String authorNameQuery = query.substring(indexOfQuery + 7).trim();

            if (!text.equals(authorNameQuery)) {
                container.removeView(button);
            }
        }
    }
}
