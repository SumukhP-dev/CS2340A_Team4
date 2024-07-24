package com.example.healthtracker.view;

import android.app.Activity;

import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.Toast;

import com.example.healthtracker.R;

import com.example.healthtracker.ViewModel.CommunityPopupViewModel;

import com.example.healthtracker.ViewModel.CommunityViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ConstraintLayout constraintLayoutCommunityPopup;
    private FrameLayout frameLayoutWorkoutPlanPopup;
    private LinearLayout containerWorkoutPlansScrollviewCommunityPopup;

    private CommunityViewModel communityViewModel;
    private CommunityPopupViewModel communityPopupViewModel;
    private DatabaseReference mDatabase;
    private EditText challengeName;
    private EditText description;
    private EditText deadline;
    private androidx.appcompat.widget.SearchView communityPopupSearchView;
    private static VisitorWorkoutPlans visitor;

    private Button publishChallenge;
    private Button createChallenge;

    private LinearLayout container;
    private androidx.appcompat.widget.SearchView searchView;

    private ArrayList<Button> listOfButtons;

    private EditText workoutPlanName;
    private EditText notes;
    private EditText sets;
    private EditText reps;
    private EditText time;
    private EditText expectedCalories;
    private Button publishWorkoutPlan;

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);

        communityPopupViewModel = new ViewModelProvider(this).get(CommunityPopupViewModel.class);
        // pop-up
        constraintLayoutCommunityPopup = view.findViewById(R.id.constraintLayout3);

        challengeName = constraintLayoutCommunityPopup.findViewById(R.id.challengeNameEditTextView);
        description = constraintLayoutCommunityPopup.findViewById(R.id.descriptionEditTextView);
        deadline = constraintLayoutCommunityPopup.findViewById(R.id.deadlineEditTextView);
        containerWorkoutPlansScrollviewCommunityPopup =
                constraintLayoutCommunityPopup.findViewById(R.id.ContainerCommunityPopup);
        publishChallenge = constraintLayoutCommunityPopup.findViewById(R.id.newChallengeButton);
      
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.container = view.findViewById(R.id.Container2);
        this.container.setVisibility(View.VISIBLE);

        searchView = view.findViewById(R.id.communitySearchView);

        communityPopupSearchView = view.findViewById(R.id.communityPopupSearchView);
        frameLayoutWorkoutPlanPopup = view.findViewById(R.id.workoutPlansPopupScreenLayout);

        createChallenge = view.findViewById(R.id.communityCreateChallengeButton);
        workoutPlanName = frameLayoutWorkoutPlanPopup.findViewById(R.id.workoutPlanNameEditTextView);
        notes = frameLayoutWorkoutPlanPopup.findViewById(R.id.notesEditTextView);
        sets = frameLayoutWorkoutPlanPopup.findViewById(R.id.setsTextNumberDecimal);
        reps = frameLayoutWorkoutPlanPopup.findViewById(R.id.repsTextNumberDecimal);
        time = frameLayoutWorkoutPlanPopup.findViewById(R.id.editTextTime);
        expectedCalories = frameLayoutWorkoutPlanPopup.findViewById(R.id.expectedCaloriesTextNumberDecimal);
        publishWorkoutPlan = frameLayoutWorkoutPlanPopup.findViewById(R.id.newWorkoutPlanButton);

        // Dismiss small screen
        constraintLayoutCommunityPopup.setVisibility(View.GONE);
        frameLayoutWorkoutPlanPopup.setVisibility(View.GONE);


        communityViewModel.removeExpiredChallenges();
        getInfoToUpdateScreen();

        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleCommunityPopupScreen();
              
            }
        });

        publishChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityViewModel.publishChallenge(
                        challengeName.getText().toString(),
                        description.getText().toString(),
                        deadline.getText().toString(),
                        communityViewModel.getUsername()
                );

                displayErrorMessages();

                hideKeyboard(requireActivity());

                constraintLayoutCommunityPopup.setVisibility(View.GONE);
                frameLayoutWorkoutPlanPopup.setVisibility(View.GONE);

                communityViewModel.removeExpiredChallenges();
                getInfoToUpdateScreen();
            }
        });

        publishWorkoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityViewModel.clearWorkoutPlanArrayList();
                communityPopupViewModel.publishWorkoutPlan(
                        workoutPlanName.getText().toString(),
                        notes.getText().toString(),
                        sets.getText().toString(),
                        reps.getText().toString(),
                        time.getText().toString(),
                        expectedCalories.getText().toString(),
                        communityPopupViewModel.getUser().getUsername());

                hideKeyboard(requireActivity());

                DatabaseReference workoutPlanRef = communityViewModel.getDatabase().getReference("WorkoutPlans");
                DatabaseReference workoutRef = workoutPlanRef.child(communityViewModel.getUsername());
                workoutRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Log.d("nameDataSnapshot", userSnapshot.child("name").getValue(String.class));
                            if (userSnapshot.child("name").getValue(String.class)
                                    .equals(workoutPlanName.getText().toString())) {
                                NewWorkoutPlan newWorkoutPlan = new NewWorkoutPlan(userSnapshot,
                                        getContext(), getParentFragmentManager(),
                                        containerWorkoutPlansScrollviewCommunityPopup);
                                visitor.visit(newWorkoutPlan);
                                communityViewModel.addToWorkoutPlanArrayList(workoutPlanName
                                        .getText().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("databaseError", "error fetching workoutPlan dataScnapshot");
                    }
                });

                // Dismiss the small screen
                frameLayoutWorkoutPlanPopup.setVisibility(View.GONE);
                constraintLayoutCommunityPopup.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextListener(
                new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        remove(CommunityFragment.this.container, query, listOfButtons);
                        Log.d("removeFromScrollview", query);

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });

        searchView.setOnCloseListener(() -> {
            getInfoToUpdateScreen();
            return true;
        });


        communityPopupSearchView.setOnQueryTextListener(
                new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        checkIfAlreadyCreated(query);
                        communityViewModel.setDuplicate(false);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });

        communityPopupSearchView.setOnCloseListener(() -> {
            getInfoToUpdateScreen();
            return true;
        });


        return view;
    }

    public void displayErrorMessages() {
        String errorMessage = "";
        if (communityViewModel.getNameErrorMessage() != null) {
            errorMessage = errorMessage + " " + communityViewModel.getNameErrorMessage();
        }
        if (communityViewModel.getDescriptionErrorMessage() != null) {
            errorMessage = errorMessage + " " + communityViewModel.getDescriptionErrorMessage();
        }
        if (communityViewModel.getDeadlineErrorMessage() != null) {
            errorMessage = errorMessage + " " + communityViewModel.getDeadlineErrorMessage();
        }

        if (communityViewModel.getWorkoutPlansMinimumErrorMessage() != null) {
            errorMessage = errorMessage + " " + communityViewModel.getWorkoutPlansMinimumErrorMessage();
        }
        if (errorMessage.length() != 0) {
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void checkIfAlreadyCreated(String query) {
        visitor = new VisitorWorkoutPlans();
        DatabaseReference workoutPlanRef = communityViewModel.getDatabase().getReference("WorkoutPlans");
        workoutPlanRef.child(communityViewModel.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (query.equals(postSnapshot.child("name").getValue(String.class))) {
                        if (!communityViewModel.getDuplicate()) {
                            communityViewModel.setDuplicate(true);
                            OldWorkoutPlan oldWorkoutPlan = new OldWorkoutPlan(postSnapshot,
                                    getContext(), getParentFragmentManager(),
                                    containerWorkoutPlansScrollviewCommunityPopup);
                            visitor.visit(oldWorkoutPlan);
                            communityViewModel.addToWorkoutPlanArrayList(query);
                            return;
                        } else {
                            return;
                        }
                    }
                }
                showCreateWorkoutPlan();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCreateWorkoutPlan() {
        communityViewModel.setDuplicate(true);
        constraintLayoutCommunityPopup.setVisibility(View.GONE);
        frameLayoutWorkoutPlanPopup.setVisibility(View.VISIBLE);

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view,
        // so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus,
        // create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void toggleCommunityPopupScreen() {
        constraintLayoutCommunityPopup.setVisibility(View.VISIBLE);
    }

    private void getInfoToUpdateScreen() {
        DatabaseReference challengeRef = mDatabase.child("Community");
        listOfButtons = new ArrayList<>();

        challengeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfButtons = new ArrayList<>();
                container.removeAllViews();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    addDataToScrollView(userSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CommunityFragment", "Error fetching workouts: " + error.getMessage());
            }
        });
    }

    public void addDataToScrollView(DataSnapshot userSnapshot) {
        String userID = userSnapshot.getKey();

        for (DataSnapshot challengeSnapshot : userSnapshot.getChildren()) {
            String challengeID = challengeSnapshot.getKey();
            String name = challengeSnapshot.child("name").getValue(String.class);
            String description = challengeSnapshot.child("description").getValue(String.class);
            String deadline = challengeSnapshot.child("deadline").getValue(String.class);

            if ((name != null) && (getContext() != null)) {
                Button challengeButton = new Button(getContext());

                challengeButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                challengeButton.setPadding(16, 16, 16, 16);
                challengeButton.setBackgroundResource(R.drawable.gray_rounded_corner);

                String buttonText = String.format("%s \t %s ", name, userID);
                challengeButton.setText(buttonText);

                challengeButton.setOnClickListener(v -> {
                    CommunityIndividualFragment detailFragment
                            = new CommunityIndividualFragment();

                    Bundle args = new Bundle();
                    args.putString("userID", userID);
                    args.putString("name", name);
                    args.putString("description", description);
                    args.putString("deadline", deadline);
                    detailFragment.setArguments(args);

                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()

                            .replace(R.id.constraintLayout4, detailFragment)

                            .addToBackStack(null)
                            .commit();
                });

                container.addView(challengeButton);
                listOfButtons.add(challengeButton);

                View spacer = new View(getContext());
                spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 8));
                container.addView(spacer);
            }
        }
    }

    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons) {
        container.removeAllViews();
        for (Button button: listOfButtons) {
            container.addView(button);
        }
        Log.d("bugcheck", String.valueOf(listOfButtons.size()));
        Log.d("query text: ", query);
        for (Button button : listOfButtons) {
            String buttonText = button.getText().toString().toLowerCase();
            Log.d("button text: ", buttonText);
            if (!(buttonText.startsWith(query))) {
                container.removeView(button);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("test", "test1");
        communityViewModel.removeExpiredChallenges();
        getInfoToUpdateScreen();
    }
}