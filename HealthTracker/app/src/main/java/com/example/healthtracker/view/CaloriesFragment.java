package com.example.healthtracker.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.healthtracker.R;
import com.example.healthtracker.model.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CaloriesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button pieButton;
    private PieChart pie;
    private TextView calorieGoal;
    private TextView calorieBurned;
    private DatabaseReference databaseRef;
    private String curCalories;
    private String username;
    private String genderInfo;
    private String heightInfo;
    private String weightInfo;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calories, container, false);

        pieButton = view.findViewById(R.id.button_dataVis);
        pie = view.findViewById(R.id.chart_dataVisualization);
        calorieGoal = view.findViewById(R.id.calorie_goal);
        calorieBurned = view.findViewById(R.id.calorie_burned);

        username = cleanUsername(User.getInstance().getUsername());
        final double[] goalCal = new double[1];
        databaseRef = FirebaseDatabase.getInstance().getReference();

        databaseRef.child("Workouts").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double totalCaloriesBurned = 0.0;
                        String stringCaloriesBurned;
                        Double doubleCaloriesBurned;

                        Calendar calendar = Calendar.getInstance();
                        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int currentMonth = calendar.get(Calendar.MONTH); // 0-indexed

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String date = String.valueOf(dataSnapshot.child("Date").child("date").getValue());
                            String month = String.valueOf(dataSnapshot.child("Date").child("month").getValue());

                            if (date.equals("null") || month.equals("null")) {
                                continue;
                            }

                            int intDate = Integer.parseInt(date);
                            int intMonth = Integer.parseInt(month);

                            stringCaloriesBurned = String.valueOf(dataSnapshot.child("caloriesBurned").getValue());
                            doubleCaloriesBurned = Double.parseDouble(stringCaloriesBurned);

                            if (intDate == currentDay && intMonth == currentMonth) {
                                totalCaloriesBurned += doubleCaloriesBurned;
                            }
                        }
                        curCalories = String.valueOf(totalCaloriesBurned);
                        calorieBurned.setText(curCalories);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Error retrieving data", error.toException());
                    }
                });

        databaseRef.child("User").child(username).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        genderInfo = String.valueOf(dataSnapshot.child("gender").getValue());
                        heightInfo = String.valueOf(dataSnapshot.child("height").getValue());
                        weightInfo = String.valueOf(dataSnapshot.child("weight").getValue());

                        if (heightInfo.equals("null") || weightInfo.equals("null")) {
                            Log.e("DataError", "Height or weight data is null");
                            return;
                        }

                        Double heightDouble = Double.parseDouble(heightInfo);
                        Double weightDouble = Double.parseDouble(weightInfo);

                        if ("male".equals(genderInfo)) {
                            goalCal[0] = goalMen(weightDouble, heightDouble, 30);
                        } else if ("female".equals(genderInfo)) {
                            goalCal[0] = goalWomen(weightDouble, heightDouble, 50);
                        } else {
                            goalCal[0] = goalMen(weightDouble, heightDouble, 30);
                        }
                        calorieGoal.setText(Double.toString(goalCal[0]));
                    }
                });

        if (pieButton != null) {
            pieButton.setOnClickListener((l) -> {
                if (pie != null && curCalories != null) {
                    drawPie(pie, curCalories, goalCal[0]);
                } else {
                    Log.e("PieChart", "Pie or curCalories is not initialized");
                }
            });
        } else {
            Log.e("PieButton", "PieButton is not initialized");
        }

        return view;
    }

    public double goalMen(double weight, double height, double age) {
        return 10 * weight + 6.25 * height - (5 * age) + 5;
    }

    public double goalWomen(double weight, double height, double age) {
        return 10 * weight + 6.25 * height - (5 * age) - 161;
    }

    public List<PieEntry> getPieEntries(String curCalories, double goalVal) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        double cal = Double.parseDouble(curCalories);

        float calFloat = (float) cal;
        float goalFloat = (float) goalVal;

        if (cal != 0) {
            entries.add(new PieEntry(calFloat, "Current burning"));
        }
        entries.add(new PieEntry(goalFloat, "Goal"));

        return entries;
    }

    public void drawPie(PieChart pie, String curCalories, double goalVal) {
        List<PieEntry> entries = getPieEntries(curCalories, goalVal);
        List<Integer> colors = new ArrayList<>();
        Random rng = new Random();
        for (int i = 0; i < entries.size(); i++) {
            colors.add(rng.nextInt());
        }

        PieDataSet set = new PieDataSet(entries, "Subjects");
        set.setColors(colors);

        pie.setData(new PieData(set));
        pie.invalidate();
    }

    // Cleans username to remove punctuation and the email handle
    public String cleanUsername(String username) {
        String usernameHandleRemoved = username.substring(0,
                username.length() - 10);
        return usernameHandleRemoved.replaceAll("\\p{P}", "");
    }
}
