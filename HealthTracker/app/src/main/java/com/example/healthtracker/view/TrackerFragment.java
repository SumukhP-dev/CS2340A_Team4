package com.example.healthtracker.view;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.EditText;
import com.example.healthtracker.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackerFragment extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatabaseReference mDatabase;
    private FrameLayout frameLayout;
    private Button showScreenButton;
    private Button logWorkoutButton;
    private EditText workoutInput, setCompleted, reps, calories, notes;

    private String mParam1;
    private String mParam2;

    public TrackerFragment() {
        // Required empty public constructor
    }

    public static TrackerFragment newInstance(String param1, String param2) {
        TrackerFragment fragment = new TrackerFragment();
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
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);

        frameLayout = view.findViewById(R.id.smallScreen);
        showScreenButton = view.findViewById(R.id.showScreenButton);
        logWorkoutButton = frameLayout.findViewById(R.id.log_workout);

        workoutInput = frameLayout.findViewById(R.id.workoutInput);
        setCompleted = frameLayout.findViewById(R.id.setCompleted);
        reps = frameLayout.findViewById(R.id.reps);
        calories = frameLayout.findViewById(R.id.calories);
        notes = frameLayout.findViewById(R.id.notes);

        showScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSmallScreen();
            }
        });

        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logWorkoutAndDismissSmallScreen();
            }
        });

        return view;
    }

    private void toggleSmallScreen() {
        /*
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
        } else {
        */
        frameLayout.setVisibility(View.VISIBLE);
        //}
    }

    private void logWorkoutAndDismissSmallScreen() {
        String workout = workoutInput.getText().toString();
        String sets = setCompleted.getText().toString();
        String repsPerSet = reps.getText().toString();
        String caloriesPerSet = calories.getText().toString();
        String workoutNotes = notes.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> user = new HashMap<>();
        user.put("additionalNotes", workoutNotes);
        user.put("caloriesBurned", caloriesPerSet);
        user.put("reps", repsPerSet);
        user.put("sets", sets);
        user.put("workoutName", workout);
        mDatabase.child("Workouts").child("bob").child("workout3").setValue(user);

        workoutInput.setText("");
        setCompleted.setText("");
        reps.setText("");
        calories.setText("");
        notes.setText("");

        System.out.println("Workout logged!");

        // Dismiss the small screen
        frameLayout.setVisibility(View.GONE);
    }
}