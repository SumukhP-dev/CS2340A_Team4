package com.example.healthtracker.view;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.PersonalInformationViewModel;
import com.example.healthtracker.ViewModel.WorkoutsViewModel;
import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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

    private DatabaseReference mDatabase;
    private ConstraintLayout constraintLayout;
    private EditText workoutPlanName;
    private EditText notes;
    private EditText sets;
    private EditText reps;
    private EditText time;
    private EditText expectedCalories;
    private Button publishWorkoutPlan;
    private Button createWorkoutPlan;
    private LinearLayout Container;


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

        constraintLayout = view.findViewById(R.id.constraintLayout2);

        workoutPlanName = constraintLayout.findViewById(R.id.workoutPlanNameEditTextView);
        notes = constraintLayout.findViewById(R.id.notesEditTextView);
        sets = constraintLayout.findViewById(R.id.setsTextNumberDecimal);
        reps = constraintLayout.findViewById(R.id.repsTextNumberDecimal);
        time = constraintLayout.findViewById(R.id.editTextTime);
        expectedCalories = constraintLayout.findViewById(R.id.expectedCaloriesTextNumberDecimal);
        publishWorkoutPlan = constraintLayout.findViewById(R.id.newWorkoutPlanButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Container = view.findViewById(R.id.Container);
        Container.setVisibility(View.VISIBLE);

        createWorkoutPlan = view.findViewById(R.id.createWorkoutPlansButton);

        getInfoToUpdateScreen();
        constraintLayout.setVisibility(View.GONE);


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

                hideKeyboard(requireActivity());

                // Dismiss the small screen

                constraintLayout.setVisibility(View.GONE);
                getInfoToUpdateScreen();

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
        constraintLayout.setVisibility(View.VISIBLE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getInfoToUpdateScreen() {
        DatabaseReference workoutPlansRef = mDatabase.child("WorkoutPlans");

        workoutPlansRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Container.removeAllViews(); // Clear existing views

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    for (DataSnapshot workoutSnapshot : userSnapshot.getChildren()) {
                        String workoutId = workoutSnapshot.getKey();
                        String cals = workoutSnapshot.child("expectedCalories").getValue(String.class);
                        String name = workoutSnapshot.child("name").getValue(String.class);
                        String notes = workoutSnapshot.child("notes").getValue(String.class);
                        String reps = workoutSnapshot.child("reps").getValue(String.class);
                        String sets = workoutSnapshot.child("sets").getValue(String.class);
                        String time = workoutSnapshot.child("time").getValue(String.class);

                        if (name != null) {
                            Button workoutButton = new Button(getContext());
                            workoutButton.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            workoutButton.setPadding(16, 16, 16, 16);
                            workoutButton.setBackgroundResource(R.drawable.gray_rounded_corner);

                            String buttonText = String.format("User: %s\t%s ", userId, name);
                            workoutButton.setText(buttonText);

                            workoutButton.setOnClickListener(v -> {
                                WorkoutsIndividualFragment detailFragment = new WorkoutsIndividualFragment();

                                // Create a Bundle to pass data to the new fragment
                                Bundle args = new Bundle();
                                args.putString("userId", userId);
                                args.putString("expectedCalories", cals);
                                args.putString("name", name);
                                args.putString("notes", notes);
                                args.putString("reps", reps);
                                args.putString("sets", sets);
                                args.putString("time", time);
                                detailFragment.setArguments(args);

                                // Perform the fragment transaction
                                FragmentManager fragmentManager = getParentFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frameLayout2, detailFragment) // Replace R.id.frameLayout2 with your container ID
                                        .addToBackStack(null)
                                        .commit();
                            });

                            Container.addView(workoutButton);

                            // Add some space between buttons
                            View spacer = new View(getContext());
                            spacer.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    8)); // 8dp height
                            Container.addView(spacer);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("WorkoutsFragment", "Error fetching workouts: " + databaseError.getMessage());
            }
        });
        /**
        mDatabase.child("WorkoutPlans")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnap = task.getResult();
                        for (DataSnapshot childSnapshot : dataSnap.getChildren()) {
                            String childKey = childSnapshot.getKey();
                            String childValue = String.valueOf(childSnapshot.getValue());
                            Log.d("childKey", String.valueOf(childKey));
                            Log.d("childValue:", childValue);
                            mDatabase.child("WorkoutPlans").child(childKey)
                                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            DataSnapshot data = task.getResult();
                                            for (DataSnapshot childData: data.getChildren()) {
                                                String workout = childData.getKey();
                                                String workoutinfo = String.valueOf(childData.getValue());
                                                Log.d("childKey", String.valueOf(workout));
                                                Log.d("childValue:", workoutinfo);
                                                mDatabase.child("WorkoutPlans").child(childKey).child(workout)
                                                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                DataSnapshot info = task.getResult();
                                                                for (DataSnapshot childInfo: info.getChildren()) {
                                                                    String infoKey = childInfo.getKey();
                                                                    String infoValue = String.valueOf(childInfo.getValue());
                                                                    TextView textView = new TextView(getContext());
                                                                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                                                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                                                            ViewGroup.LayoutParams.WRAP_CONTENT));
                                                                    textView.setPadding(16, 16, 16, 16);

                                                                    String displayText = String.format("%s: %b", infoKey, infoValue);
                                                                    textView.setText(displayText);

                                                                    // Add the TextView to the container
                                                                    Container.addView(textView);
                                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
        **/
    }
}