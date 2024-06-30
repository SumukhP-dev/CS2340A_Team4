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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button pieButton;
    private PieChart pie;

    private TextView calorie_goal;
    private TextView calorie_burned;

    private DatabaseReference databaseRef;

    private String curCalries;

    private String username;

    private String genderInfo;
    private String heightInfo;
    private String weightInfo;
    // private String age;


    public CaloriesFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutsFragment.
     */
    // Rename and change types and number of parameters
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

        View view = inflater.inflate(R.layout.fragment_calories, container, false);

        pieButton = view.findViewById(R.id.button_dataVis);
        pie=view.findViewById(R.id.chart_dataVisualization);
        calorie_goal = view.findViewById(R.id.calorie_goal);
        calorie_burned = view.findViewById(R.id.calorie_burned);




        username= User.getInstance().getUsername();

        final double[] goalCal = new double[1];

        databaseRef= FirebaseDatabase.getInstance().getReference();

        // =========================
//        databaseRef.child("Workouts").child(username).child("workout1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                DataSnapshot dataSnapshot=task.getResult();
//                curCalries=String.valueOf(dataSnapshot.child("caloriesBurned").getValue());
//                System.out.println("current calories burned: " + curCalries);
//            }
//        });
        // =========================
        databaseRef.child("Workouts").child("arnava2004").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double totalCaloriesBurned = 0.0;
                String stringCaloriesBurned;
                Double doubleCaloriesBurned;

                String Date;
                String Month;
                int intDate;
                int intMonth;
                //

                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH); // 0-indexed


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Date=String.valueOf(dataSnapshot.child("Date").child("date").getValue());
                    Month=String.valueOf(dataSnapshot.child("Date").child("month").getValue());
                    intDate=Integer.parseInt(Date);
                    intMonth=Integer.parseInt(Month);

                    stringCaloriesBurned = String.valueOf(dataSnapshot.child("caloriesBurned").getValue());
                    doubleCaloriesBurned =  Double.parseDouble(stringCaloriesBurned);

                    if (intDate==currentDay && intMonth==currentMonth){
                        totalCaloriesBurned += doubleCaloriesBurned;
                    }

                }
                curCalries = String.valueOf(totalCaloriesBurned);
                calorie_burned.setText(curCalries);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // =========================

        databaseRef.child("User").child("arnava2004").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot=task.getResult();
                genderInfo=String.valueOf(dataSnapshot.child("gender").getValue());

                heightInfo=String.valueOf(String.valueOf(dataSnapshot.child("height").getValue()));
                Double heightDouble = Double.parseDouble(heightInfo);


                weightInfo=String.valueOf(dataSnapshot.child("weight").getValue());
                Double weightDouble = Double.parseDouble(weightInfo);

                if (genderInfo == "male"){
                    goalCal[0] = goalMen(weightDouble,heightDouble, 30);
                }else if (genderInfo == "female"){
                    goalCal[0] = goalWomen(weightDouble,heightDouble,50);
                } else {
                    goalCal[0] = goalMen(weightDouble,heightDouble,30);
                }
                calorie_goal.setText(Double.toString(goalCal[0]));

            }
        });


        if (pieButton != null) {
            pieButton.setOnClickListener((l) -> {
                if (pie != null && curCalries != null) {
                    drawPie(pie, curCalries, goalCal[0]);
                } else {
                    Log.e("PieChart", "Pie or curCalries is not initialized");
                }
            });
        } else {
            Log.e("PieButton", "PieButton is not initialized");
        }

        // pieButton.setOnClickListener((l) ->{drawPie(pie,curCalries,goalCal);});
        return view;
    }

    public double goalMen(double weight,double height,double age){
        return 10*weight+6.25*height-(5*age)+5;
    }

    public double goalWomen(double weight,double height,double age){
        return 10*weight+6.25*height-(5*age)-161;
    }

    public List<PieEntry> getPieEntries(String curCalories, double goalVal) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        double cal = Double.parseDouble(curCalories);

        float calFloat = (float) cal;
        float goalFloat = (float) goalVal;

        entries.add(new PieEntry(calFloat, "Current burning"));
        entries.add(new PieEntry(goalFloat, "Goal"));

        return entries;
    }

    public void drawPie(PieChart pie, String curCalries, double goalVal) {
        List<PieEntry> entries = getPieEntries(curCalries, goalVal);

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
}