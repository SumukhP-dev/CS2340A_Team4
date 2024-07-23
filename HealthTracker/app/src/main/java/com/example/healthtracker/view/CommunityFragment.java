package com.example.healthtracker.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
    // --
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

    private WorkoutPlanNameSearchStrategy workoutPlanNameSearchStrategy;
    private WorkoutPlanAuthorSearchStrategy workoutPlanAuthorSearchStrategy;

    private ArrayList<Button> listOfButtons;
    private SearchModel searchModel = new SearchModel();

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

        constraintLayoutCommunityPopup = view.findViewById(R.id.constraintLayout5);

        challengeName = constraintLayoutCommunityPopup.findViewById(R.id.nameCommunityChallengeEditTextView);
        description = constraintLayoutCommunityPopup.findViewById(R.id.descriptionCommunityChallengeEditTextView);
        deadline = constraintLayoutCommunityPopup.findViewById(R.id.deadlineCommunityChallengeEditTextDate);

        publishChallenge = constraintLayoutCommunityPopup.findViewById(R.id.newCommunityWorkoutButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.container = view.findViewById(R.id.Container2);
        this.container.setVisibility(View.VISIBLE);

        searchView = view.findViewById(R.id.communitySearchView);
        communityPopupSearchView = view.findViewById(R.id.communityPopupSearchView);
        containerWorkoutPlansScrollviewCommunityPopup = view.findViewById(R.id.ContainerCommunityPopup);
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

                hideKeyboard(requireActivity());

                constraintLayoutCommunityPopup.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(
                new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        workoutPlanNameSearchStrategy = new WorkoutPlanNameSearchStrategy();
                        searchModel.setStrategy(workoutPlanNameSearchStrategy);
                        searchModel.remove(CommunityFragment.this.container, query, listOfButtons);
                        workoutPlanAuthorSearchStrategy = new WorkoutPlanAuthorSearchStrategy();
                        searchModel.setStrategy(workoutPlanAuthorSearchStrategy);
                        searchModel.remove(CommunityFragment.this.container, query, listOfButtons);
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
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });

        communityPopupSearchView.setOnCloseListener(() -> {
            return false;
        });

        return view;
    }

    public void checkIfAlreadyCreated(String query) {
        visitor = new VisitorWorkoutPlans();
        DatabaseReference workoutPlanRef = communityViewModel.getDatabase().getReference("WorkoutPlans");
        workoutPlanRef.child(communityViewModel.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (query.equals(postSnapshot.child("name").getValue())) {
                        OldWorkoutPlan oldWorkoutPlan = new OldWorkoutPlan(postSnapshot,
                                getContext(), getParentFragmentManager(),
                                containerWorkoutPlansScrollviewCommunityPopup);
                        visitor.visit(oldWorkoutPlan);
                        communityViewModel.addToWorkoutPlanArrayList(query);
                        return;
                    }
                }
                createWorkoutPlan();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createWorkoutPlan() {
        constraintLayoutCommunityPopup.setVisibility(View.GONE);
        frameLayoutWorkoutPlanPopup.setVisibility(View.VISIBLE);

        publishWorkoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                DatabaseReference workoutRef = workoutPlanRef.child(workoutPlanName.getText().toString());
                workoutRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NewWorkoutPlan newWorkoutPlan = new NewWorkoutPlan(dataSnapshot,
                                getContext(), getParentFragmentManager(),
                                containerWorkoutPlansScrollviewCommunityPopup);
                        visitor.visit(newWorkoutPlan);
                        communityViewModel.addToWorkoutPlanArrayList(workoutPlanName
                                .getText().toString());
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

    //TODO: Create method to refresh scrollview
    public void getInfoToUpdateScreen() {

    }
}