package com.example.healthtracker.view;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.CaloriesViewModel;
import com.example.healthtracker.model.User;
import com.github.mikephil.charting.charts.PieChart;



public class CaloriesFragment extends Fragment {

    private CaloriesViewModel caloriesViewModel;
    private ConstraintLayout constraintLayout;
    private TextView caloriesBurned;
    private TextView calorieGoal;
    private Button buttonDataVis;
    private PieChart pieChart; // ⁉️

    public CaloriesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_calories, container, false);
       constraintLayout = view.findViewById(R.id.constraint_layout_ID);
       caloriesBurned = constraintLayout.findViewById(R.id.calorie_burned);
       calorieGoal = constraintLayout.findViewById(R.id.calorie_goal);
       buttonDataVis = constraintLayout.findViewById(R.id.button_dataVis);
       pieChart = constraintLayout.findViewById(R.id.chart_dataVisualization);

       buttonDataVis.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               drawPie();
           }
       });

        caloriesViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    int calorieGoal = calculateCalorieGoal(user);
                    calorieGoalTextView.setText("Calorie Goal: " + calorieGoal);
                }
            }
        });

        return view;
    }

    private int calculateCalorieGoal(User user) {
        // Use your specific formula to calculate the calorie goal based on user data
        // Example formula: (10 * weight) + (6.25 * height) - (5 * age) + 5 (for males)
        return 2000; // Placeholder value
    }

    private void drawPie() {

    }
}