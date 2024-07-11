package com.example.healthtracker.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.PersonalInformationViewModel;
import com.example.healthtracker.ViewModel.WorkoutsViewModel;
import com.example.healthtracker.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutsFragment extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private WorkoutsViewModel workoutsViewModel;

    private FrameLayout frameLayout;
    private EditText workoutPlanName;
    private EditText notes;
    private EditText sets;
    private EditText reps;
    private EditText time;
    private EditText expectedCalories;
    private Button publishWorkoutPlan;
    private Button createWorkoutPlan;

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkoutsFragment() {
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
    public static WorkoutsFragment newInstance(String param1, String param2) {
        WorkoutsFragment fragment = new WorkoutsFragment();
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
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        workoutsViewModel = new ViewModelProvider(this)
                .get(WorkoutsViewModel.class);

        frameLayout = view.findViewById(R.id.workoutPlansPopupScreenLayout);

        workoutPlanName = frameLayout.findViewById(R.id.workoutPlanNameEditTextView);
        notes = frameLayout.findViewById(R.id.notesEditTextView);
        sets = frameLayout.findViewById(R.id.setsTextNumberDecimal);
        reps = frameLayout.findViewById(R.id.repsTextNumberDecimal);
        time = frameLayout.findViewById(R.id.editTextTime);
        expectedCalories = frameLayout.findViewById(R.id.expectedCaloriesTextNumberDecimal);
        publishWorkoutPlan = frameLayout.findViewById(R.id.newWorkoutPlanButton);

        createWorkoutPlan = view.findViewById(R.id.createWorkoutPlansButton);

        createWorkoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSmallScreen();
            }
        });

        publishWorkoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutsViewModel.publishWorkoutPlan(
                        workoutPlanName.getText().toString(),
                        notes.getText().toString(),
                        sets.getText().toString(),
                        reps.getText().toString(),
                        time.getText().toString(),
                        expectedCalories.getText().toString(),
                        workoutsViewModel.getUsername());
                displayErrorMessages();
            }
        });

        return view;
    }

    public void displayErrorMessages() {
        String errorMessage = "";
        if (workoutsViewModel.getCaloriesErrorMessage() != null) {
            errorMessage += workoutsViewModel.getCaloriesErrorMessage();
        }
        if (workoutsViewModel.getNameErrorMessage() != null) {
            errorMessage = errorMessage + " " + workoutsViewModel.getNameErrorMessage();
        }
        if (errorMessage.length() != 0) {
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void toggleSmallScreen() {
        frameLayout.setVisibility(View.VISIBLE);
    }
}