package com.example.healthtracker.view;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.CommunityViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityIndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityIndividualFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ConstraintLayout constraintLayout;

    private TextView setChallenger;
    private TextView setChallengeName;
    private TextView setDeadline;
    private TextView setDescription;
    private ImageButton back;
    private Button acceptChallengeButton;
    private Button completeChallengeButton;

    private CommunityViewModel communityViewModel;
    private DatabaseReference mDatabase;

    private LinearLayout participantsContainer;
    private LinearLayout workoutPlansContainer;

    private CommunityConcreteSubject challengeStatus;

    public CommunityIndividualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityIndividualFragment.
     */
    public static CommunityIndividualFragment newInstance(String param1, String param2) {
        CommunityIndividualFragment fragment = new CommunityIndividualFragment();
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
        View view = inflater.inflate(R.layout.fragment_community_individual, container, false);

        constraintLayout = view.findViewById(R.id.frameLayout5);

        setChallenger = constraintLayout.findViewById(R.id.challengerNameTextView);
        setChallengeName = constraintLayout.findViewById(R.id.communityWorkoutPlanTitleTextView);
        setDeadline = constraintLayout.findViewById(R.id.deadlineDataTextView);
        setDescription = constraintLayout.findViewById(R.id.descriptionDataTextView);

        participantsContainer = view.findViewById(R.id.ContainerCommunity);
        workoutPlansContainer = view.findViewById(R.id.ContainerWorkoutPlans);

        acceptChallengeButton = constraintLayout.findViewById(R.id.challengeButton);
        completeChallengeButton = constraintLayout.findViewById(R.id.completeChallengeButton);
        back = constraintLayout.findViewById(R.id.communityBackButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        communityViewModel = new ViewModelProvider(requireActivity()).get(CommunityViewModel.class);

        challengeStatus = new CommunityConcreteSubject();

        Bundle args = getArguments();

        if (args != null) {
            String userID = args.getString("userID");
            String name = args.getString("name");
            String description = args.getString("description");
            String deadline = args.getString("deadline");
            setChallenger.setText(userID);
            setChallengeName.setText(name);
            setDescription.setText(description);
            setDeadline.setText(deadline);

            getParticipantsToUpdateScreen(userID, name);
            getWorkoutPlansToUpdateScreen(userID, name);

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        acceptChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentChallengerAuthor = setChallenger.getText().toString();
                String currentChallengeName = setChallengeName.getText().toString();
                String currentUser = communityViewModel.getUsername();

                DatabaseReference mDatabase = communityViewModel.getDatabase().getReference();

                mDatabase.child("Community").child(currentChallengerAuthor)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot challengeSnapshot : snapshot.getChildren()) {
                                    String challengeName = challengeSnapshot.child("name")
                                            .getValue(String.class);
                                    challengeName = challengeName.toLowerCase();

                                    if (challengeName != null && challengeName
                                            .equals(currentChallengeName.toLowerCase())) {
                                        challengeSnapshot.getRef().child("participants")
                                                .child(currentUser).setValue("accepted");

                                        challengeStatus
                                                .notifyObservers(currentUser, "accepted");

                                        acceptChallengeButton.setVisibility(View.GONE);
                                        completeChallengeButton.setVisibility(View.VISIBLE);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        completeChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentChallengerAuthor = setChallenger.getText().toString();
                String currentChallengeName = setChallengeName.getText().toString();
                String currentUser = communityViewModel.getUsername();

                DatabaseReference mDatabase = communityViewModel.getDatabase().getReference();

                mDatabase.child("Community").child(currentChallengerAuthor)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot challengeSnapshot : snapshot.getChildren()) {
                                    String challengeName = challengeSnapshot.child("name")
                                            .getValue(String.class);
                                    challengeName = challengeName.toLowerCase();

                                    if (challengeName != null && challengeName
                                            .equals(currentChallengeName.toLowerCase())) {
                                        challengeSnapshot.getRef().child("participants")
                                                .child(currentUser).setValue("completed");

                                        challengeStatus.notifyObservers(currentUser, "completed");

                                        completeChallengeButton.setVisibility(View.GONE);

                                        break;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        return view;
    }

    private void getParticipantsToUpdateScreen(String user, String name) {
        DatabaseReference participantRef = mDatabase.child("Community").child(user);
        participantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                participantsContainer.removeAllViews();
                for (DataSnapshot challengeSnapshot : snapshot.getChildren()) {
                    String challengeName = challengeSnapshot.child("name").getValue(String.class);


                    if (challengeName.equals(name)) {


                        for (DataSnapshot participant
                                : challengeSnapshot.child("participants").getChildren()) {

                            addParticipantView(participant.getKey(),
                                    participant.getValue(String.class));
                        }

                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CommunityIndividualFragment", "Error fetching participants");
            }
        });

    }

    private void addParticipantView(String participantID, String status) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View participantView = inflater
                .inflate(R.layout.button_community_individual_participant, null);

        TextView participantTextView = participantView.findViewById(R.id.participantsTextView);
        TextView statusTextView = participantView.findViewById(R.id.statusTextView);
        ImageView checkMarkImageView = participantView.findViewById(R.id.checkMarkImageView);

        participantTextView.setText(participantID);
        statusTextView.setText(status);

        if (statusTextView.getText().toString().equals("completed")) {
            checkMarkImageView.setVisibility(View.VISIBLE);
        } else {
            checkMarkImageView.setVisibility(View.GONE);
        }

        CommunityConcreteObserver participantObserver
                = new CommunityConcreteObserver(participantID, statusTextView);
      
        challengeStatus.addObserver(participantObserver);

        participantsContainer.addView(participantView);
    }

    // user is the name of the community challenge creator
    private void getWorkoutPlansToUpdateScreen(String user, String name) {
        DatabaseReference communityRef = mDatabase.child("Community").child(user);
        communityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workoutPlansContainer.removeAllViews();
                for (DataSnapshot challengeSnapshot: snapshot.getChildren()) {
                    String challengeName = challengeSnapshot.child("name").getValue(String.class);
                    System.out.println("iterating. currently at: " + challengeName);

                    if (challengeName.equals(name)) {
                        System.out.println("found a match! " + challengeName + " equals " + name);

                        for (DataSnapshot workoutPlans
                                : challengeSnapshot.child("workoutPlans").getChildren()) {
                            addWorkoutPlansView(user, workoutPlans.getValue(String.class));
                        }

                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CommunityIndividualFragment", "Error fetching participants");
            }
        });

    }

    // userID is the name of the community challenge creator
    private void addWorkoutPlansView(String userID, String workoutPlanName) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        if ((workoutPlanName != null) && (getContext() != null)) {
            Button workoutPlanButton = new Button(getContext());

            workoutPlanButton.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            workoutPlanButton.setPadding(16, 16, 16, 16);
            workoutPlanButton.setBackgroundResource(R.drawable.gray_rounded_corner);

            String buttonText = String.format("%s \t %s ", workoutPlanName, userID);
            workoutPlanButton.setText(buttonText);

            workoutPlanButton.setOnClickListener(v -> {
                WorkoutsIndividualFragment detailFragment
                        = new WorkoutsIndividualFragment();

                DatabaseReference workoutPlansRef = mDatabase.child("WorkoutPlans").child(userID);
                workoutPlansRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot workoutPlanSnapshot
                                : snapshot.getChildren()) {
                            String workoutPlanNameQueried = workoutPlanSnapshot
                                    .child("name").getValue(String.class);
                            if (workoutPlanName.equals(workoutPlanNameQueried)) {
                                // Create a Bundle to pass data to the new fragment
                                Bundle args = new Bundle();
                                args.putString("userId", userID);
                                args.putString("expectedCalories",
                                        workoutPlanSnapshot.child("expectedCalories")
                                                .getValue(String.class));
                                args.putString("name", workoutPlanSnapshot.child("name")
                                        .getValue(String.class));
                                args.putString("notes", workoutPlanSnapshot.child("notes")
                                        .getValue(String.class));
                                args.putString("reps", workoutPlanSnapshot.child("reps")
                                        .getValue(String.class));
                                args.putString("sets", workoutPlanSnapshot.child("sets")
                                        .getValue(String.class));
                                args.putString("time", workoutPlanSnapshot.child("time")
                                        .getValue(String.class));
                                detailFragment.setArguments(args);

                                // Perform the fragment transaction
                                FragmentManager fragmentManager = getParentFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frameLayout5, detailFragment)
                                        // Replace R.id.frameLayout2 with your container ID
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            });

            workoutPlansContainer.addView(workoutPlanButton);

            View spacer = new View(getContext());
            spacer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 8));
            workoutPlansContainer.addView(spacer);
        }
    }

}