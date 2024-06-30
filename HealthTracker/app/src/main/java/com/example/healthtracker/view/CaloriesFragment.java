package com.example.healthtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthtracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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

    private DatabaseReference databaseRef;

    private String curCalries;


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
       //
        View view = inflater.inflate(R.layout.fragment_calories, container, false);

        pieButton = view.findViewById(R.id.button_dataVis);
        pie=view.findViewById(R.id.chart_dataVisualization);

        databaseRef= FirebaseDatabase.getInstance().getReference("Workouts");
        databaseRef.child("boyucheng").child("workout1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot=task.getResult();
                curCalries=String.valueOf(dataSnapshot.child("caloriesBurned").getValue());
            }
        });

        pieButton.setOnClickListener((l) ->{drawPie(pie,curCalries);});
        return view;
    }

    public void drawPie(PieChart pie, String curCalries){

        ArrayList<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        Random rng = new Random();
        double cal = new Double(curCalries).doubleValue();
        float calFloat = (float) cal;
        entries.add(new PieEntry(calFloat,"Current burning"));
        colors.add(rng.nextInt());
        entries.add(new PieEntry(200f,"Goal"));
        colors.add(rng.nextInt());

        PieDataSet set = new PieDataSet(entries,"Subjects");
        set.setColors(colors);

        pie.setData(new PieData(set));
        pie.invalidate();
    }
}