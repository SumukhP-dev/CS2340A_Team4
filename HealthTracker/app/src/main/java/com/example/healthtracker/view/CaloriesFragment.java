package com.example.healthtracker.view;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import com.example.healthtracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.DatabaseReference;


public class CaloriesFragment extends Fragment {

    private DatabaseReference database;
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

       buttonDataVis.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               drawPie();
           }
       });

        return view;
    }

    private void drawPie() {

    }
}