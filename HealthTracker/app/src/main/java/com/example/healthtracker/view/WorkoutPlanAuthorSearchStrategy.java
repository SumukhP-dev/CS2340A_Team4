package com.example.healthtracker.view;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class WorkoutPlanAuthorSearchStrategy implements Strategy {
    @Override
    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons) {
        for (Button button: listOfButtons) {
            String buttonText = button.getText().toString();
            int indexOfText = buttonText.indexOf("\t");
            String text = buttonText.substring(indexOfText + 1);
            Log.d("test2_1", text);

            int indexOfQuery = query.indexOf("author:");
            Log.d("test2_2", String.valueOf(indexOfQuery));

            if (indexOfQuery != -1) {
                String authorNameQuery = query.substring(indexOfQuery + 7).trim();
                Log.d("test2_3", authorNameQuery);
                Log.d("test2_4", String.valueOf(text.substring(0, authorNameQuery.length()).equals(authorNameQuery)));

                if (!text.substring(0, authorNameQuery.length()).equals(authorNameQuery)) {
                    container.removeView(button);
                }
            }
        }
    }
}
