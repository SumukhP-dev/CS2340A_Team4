package com.example.healthtracker.ViewModel;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

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

public class CaloriesViewModel extends ViewModel {
    private User user;

    private DatabaseReference databaseRef;

    private String curCalories;

    public CaloriesViewModel() {
        user = User.getInstance();
    }

    public void caloriesBurned(String username, TextView calorieBurned) {
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
                            String date = String.valueOf(dataSnapshot
                                    .child("Date").child("date").getValue());
                            String month = String.valueOf(dataSnapshot
                                    .child("Date").child("month").getValue());

                            if (date.equals("null") || month.equals("null")) {
                                continue;
                            }

                            int intDate = Integer.parseInt(date);
                            int intMonth = Integer.parseInt(month);

                            stringCaloriesBurned = String.valueOf(dataSnapshot
                                    .child("caloriesBurned").getValue());
                            doubleCaloriesBurned =
                                    Double.parseDouble(stringCaloriesBurned);

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

    }
    public void caloriesGoal(String username, double[] goalCal, TextView calorieGoal) {
        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("User").child(username).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String genderInfo;
                        String heightInfo;
                        String weightInfo;
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
                            //goalCal[0] = goalMen(weightDouble, heightDouble, 30);
                        } else if ("female".equals(genderInfo)) {
                            goalCal[0] = goalWomen(weightDouble, heightDouble, 50);
                            //goalCal[0] = goalWomen(weightDouble, heightDouble, 50);
                        } else {
                            goalCal[0] = goalMen(weightDouble, heightDouble, 30);
                            //goalCal[0] = goalMen(weightDouble, heightDouble, 30);
                        }
                        calorieGoal.setText(Double.toString(goalCal[0]));
                    }
                });


    }

    public void draw(String username, Button pieButton, PieChart pie, double[] goalCal) {
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
                            String date = String.valueOf(dataSnapshot
                                    .child("Date").child("date").getValue());
                            String month = String.valueOf(dataSnapshot
                                    .child("Date").child("month").getValue());

                            if (date.equals("null") || month.equals("null")) {
                                continue;
                            }

                            int intDate = Integer.parseInt(date);
                            int intMonth = Integer.parseInt(month);

                            stringCaloriesBurned = String.valueOf(dataSnapshot
                                    .child("caloriesBurned").getValue());
                            doubleCaloriesBurned =
                                    Double.parseDouble(stringCaloriesBurned);

                            if (intDate == currentDay && intMonth == currentMonth) {
                                totalCaloriesBurned += doubleCaloriesBurned;
                            }
                        }
                        curCalories = String.valueOf(totalCaloriesBurned);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Error retrieving data", error.toException());
                    }
                });

        if (pieButton != null) {
            pieButton.setOnClickListener((l) -> {
                if (pie != null && curCalories != null) {
                    drawPie(pie, curCalories, goalCal[0]);
                    //drawPie(pie, curCalories, goalCal[0]);
                } else {
                    Log.e("PieChart", "Pie or curCalories is not initialized");
                }
            });
        } else {
            Log.e("PieButton", "PieButton is not initialized");
        }
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

    public String cleanUsername(String username) {
        String usernameHandleRemoved = username;
        if (username.length() >= 10
                && username.substring(username.length() - 10)
                .equals("@gmail.com")) {
            usernameHandleRemoved = username.substring(0,
                    username.length() - 10);
        }
        return usernameHandleRemoved.replaceAll("\\p{P}", "");
    }
}
