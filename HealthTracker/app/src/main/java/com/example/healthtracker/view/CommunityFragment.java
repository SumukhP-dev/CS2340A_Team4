package com.example.healthtracker.view;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.CommunityViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private ConstraintLayout constraintLayout;

    // --
    private CommunityViewModel communityViewModel;
    private DatabaseReference mDatabase;
    private ConstraintLayout constraintLayout;
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

        // pop-up

        constraintLayout = view.findViewById(R.id.constraintLayout5);

        challengeName = constraintLayout.findViewById(R.id.nameCommunityChallengeEditTextView);
        description = constraintLayout.findViewById(R.id.descriptionCommunityChallengeEditTextView);
        deadline = constraintLayout.findViewById(R.id.deadlineCommunityChallengeEditTextDate);

        publishChallenge = constraintLayout.findViewById(R.id.newCommunityWorkoutButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.container = view.findViewById(R.id.Container2);
        this.container.setVisibility(View.VISIBLE);

        searchView = view.findViewById(R.id.communitySearchView);
        communityPopupSearchView = view.findViewById(R.id.communityPopupSearchView);

        createChallenge = view.findViewById(R.id.communityCreateChallengeButton);

        // Dismiss small screen
        constraintLayout.setVisibility(View.GONE);

        getInfoToUpdateScreen();

        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSmallScreen();
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

                constraintLayout.setVisibility(View.GONE);

                getInfoToUpdateScreen();
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

                        addDataToPopupScrollView(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });

        communityPopupSearchView.setOnCloseListener(() -> {
            //
            return true;
        });

        return view;
    }

    public void addDataToPopupScrollView(String query) {
        visitor = new VisitorWorkoutPlans();

    }
}