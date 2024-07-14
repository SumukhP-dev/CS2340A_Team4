package com.example.healthtracker.view;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class WorkoutPlanNameSearchStrategy implements Strategy {
    @Override
    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons) {
        for (Button button: listOfButtons) {
            String buttonText = button.getText().toString();
            Log.d("test1", buttonText);
            String text = buttonText.substring(0, query.length());
            Log.d("test2", text);
            Log.d("test3", String.valueOf(text.equals(query)));
            if ((!(text.equals(query))) && (!query.contains("author:"))) {
                container.removeView(button);
            }
        }
    }
}
