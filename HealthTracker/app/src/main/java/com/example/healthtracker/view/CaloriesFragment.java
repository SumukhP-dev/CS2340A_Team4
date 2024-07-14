package com.example.healthtracker.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.CaloriesViewModel;
import com.example.healthtracker.model.User;
import com.github.mikephil.charting.charts.PieChart;

public class CaloriesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private CaloriesViewModel caloriesViewModel;
    private Button pieButton;
    private PieChart pie;
    private TextView calorieGoal;
    private TextView calorieBurned;
    private String username;


    public CaloriesFragment() {
        // Required empty public constructor
    }

    public static CaloriesFragment newInstance(String param1, String param2) {
        CaloriesFragment fragment = new CaloriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calories, container,
                false);
        caloriesViewModel = new ViewModelProvider(this)
                .get(CaloriesViewModel.class);

        pieButton = view.findViewById(R.id.button_dataVis);
        pie = view.findViewById(R.id.chart_dataVisualization);
        calorieGoal = view.findViewById(R.id.calorie_goal);
        calorieBurned = view.findViewById(R.id.calorie_burned);

        username = caloriesViewModel.cleanUsername(User.getInstance().getUsername());

        final double[] goalCal = new double[1];


        caloriesViewModel.caloriesBurned(username, calorieBurned);

        caloriesViewModel.caloriesGoal(username, goalCal, calorieGoal);

        caloriesViewModel.draw(username, pieButton, pie, goalCal);

        return view;
    }


}
