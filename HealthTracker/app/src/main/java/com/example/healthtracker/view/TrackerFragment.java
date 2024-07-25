package com.example.healthtracker.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthtracker.R;
import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;




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
    private LinearLayout spinnerContainer;
    private String workoutString;
    private int spinnerCount = 0;
    private String username = User.getInstance().getUsername();
    private EditText workoutInput;
    private EditText setCompleted;
    private EditText reps;
    private EditText calories;
    private EditText notes;

    private String workoutNa;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);

        frameLayout = view.findViewById(R.id.smallScreen);
        showScreenButton = view.findViewById(R.id.showScreenButton);
        logWorkoutButton = frameLayout.findViewById(R.id.log_workout);
        spinnerContainer = view.findViewById(R.id.spinnerContainer);
        spinnerContainer.setVisibility(View.VISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        workoutInput = frameLayout.findViewById(R.id.workoutPlanNameEditTextView);
        setCompleted = frameLayout.findViewById(R.id.timeEditTextView);
        reps = frameLayout.findViewById(R.id.reps);
        calories = frameLayout.findViewById(R.id.expectedCaloriesEditTextView);
        notes = frameLayout.findViewById(R.id.notesEditTextView);

        Log.d("username: ", username);
        mDatabase.child("User").child(username)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnap = task.getResult();
                        String workoutNum = String.valueOf(dataSnap.child("Counter").getValue());
                        if (workoutNum.equals("null")) {
                            workoutNum = "0";
                        }
                        spinnerCount = Integer.parseInt(workoutNum);
                        Log.d("counter inside class:", String.valueOf(spinnerCount));
                        for (int i = 0; i < spinnerCount && i < 5; i++) {
                            int counter = spinnerCount - i;
                            mDatabase.child("Workouts").child(username)
                                    .child("workout "
                                            + String.valueOf(counter))
                                    .get().addOnCompleteListener(
                                            new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull
                                                                       Task<DataSnapshot> task) {
                                                    DataSnapshot dataSnap = task
                                                            .getResult();
                                                    // Adding workoutNa
                                                    String workoutNa = String.valueOf(
                                                            dataSnap.child("workoutName")
                                                                    .getValue());

                                                    // Forgot to add this comment :)
                                                    // Adding Date

                                                    String date = String
                                                            .valueOf(dataSnap
                                                                    .child("Date").getValue());

                                                    TextView textView = new TextView(getContext());
                                                    textView.setLayoutParams(
                                                            new LinearLayout.LayoutParams(
                                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT));
                                                    textView.setPadding(16, 16,
                                                            16, 16);

                                                    String displayText = String

                                                            .format("%s    Workout: %s",
                                                                    date, workoutNa);

                                                    textView.setText(displayText);

                                                    // Add the TextView to the container
                                                    spinnerContainer.addView(textView);
                                                }
                                            });
                        }
                    }
                });
        Log.d("counter:", String.valueOf(spinnerCount));

        for (int i = 0; i < spinnerCount && i < 5; i++) {
            mDatabase.child("Workouts").child(username)
                    .child("workout " + String.valueOf(spinnerCount - i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            DataSnapshot dataSnap = task.getResult();
                            String workout = String.valueOf(
                                    dataSnap.child("workoutName").getValue());
                            workoutNa = workout;
                        }
                    });

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(16, 16, 16, 16);

            String displayText = String.format("Workout: %s", workoutNa);
            textView.setText(displayText);

            // Add the TextView to the container
            spinnerContainer.addView(textView);
        }

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




        Map<String, Object> user = new HashMap<>();
        user.put("additionalNotes", workoutNotes);
        user.put("caloriesBurned", caloriesPerSet);
        user.put("reps", repsPerSet);
        user.put("sets", sets);
        user.put("workoutName", workout);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDateandTime = sdf.format(new Date());

        user.put("Date", currentDateandTime);


        mDatabase.child("User").child(username)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                            DataSnapshot dataSnap = task.getResult();
                            String workoutNum = String.valueOf(
                                    dataSnap.child("Counter").getValue());
                            if (workoutNum.equals("null")) {
                                workoutNum = String.valueOf(0);
                            }
                            workoutString = "workout "
                                    + String.valueOf(Integer
                                    .valueOf(workoutNum) + 1);
                            mDatabase.child("User").child(username)
                                    .child("Counter").setValue(
                                            String.valueOf(Integer.valueOf(workoutNum) + 1));
                            mDatabase.child("Workouts")
                                    .child(username).child(workoutString).setValue(user);
                    }
                });

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setPadding(16, 16, 16, 16);

        // Set the text
        String displayText = String.format("%s    Workout: %s", currentDateandTime, workout);
        textView.setText(displayText);

        // Add the TextView to the container
        spinnerContainer.addView(textView);


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