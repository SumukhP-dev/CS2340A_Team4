package com.example.healthtracker.view;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.CommunityViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityIndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityIndividualFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout constraintLayout;

    private TextView setChallenger;

    private TextView setChallengeName;

    private TextView setDeadline;

    private TextView setDescription;

    private ScrollView setParticipants;

    private ImageButton back;

    private Button acceptChallengeButton;

    private Button completeChallengeButton;

    private CommunityViewModel communityViewModel;
    private DatabaseReference mDatabase;

    private ArrayList<View> listOfParticipants;

    private LinearLayout participantsContainer;

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
    // TODO: Rename and change types and number of parameters
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
        setParticipants = constraintLayout.findViewById(R.id.communityRecyclerView); // TODO: check functionality works as intended
        participantsContainer = view.findViewById(R.id.Container3); // TODO: see works as intended

        acceptChallengeButton = constraintLayout.findViewById(R.id.challengeButton);
        completeChallengeButton = constraintLayout.findViewById(R.id.completeChallengeButton);

        back = constraintLayout.findViewById(R.id.communityBackButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        communityViewModel = new ViewModelProvider(requireActivity()).get(CommunityViewModel.class);



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
                // System.out.println("hello"); -- button clicks work
                String currentChallengerAuthor = setChallenger.getText().toString();
                String currentChallengeName = setChallengeName.getText().toString();
                String currentUser = communityViewModel.getUsername();

                DatabaseReference mDatabase = communityViewModel.getDatabase().getReference();

                mDatabase.child("Community").child(currentChallengerAuthor).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot challengeSnapshot : snapshot.getChildren()) {
                            String challengeName = challengeSnapshot.child("name").getValue(String.class);
                            challengeName = challengeName.toLowerCase();

                            if (challengeName != null && challengeName.equals(currentChallengeName.toLowerCase())) {

                                challengeSnapshot.getRef().child("participants").child(currentUser).setValue("accepted");

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

        return view;
    }

    private void getParticipantsToUpdateScreen(String user, String name) {
        DatabaseReference participantRef = mDatabase.child("Community").child(user);
        listOfParticipants = new ArrayList<>();
        participantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfParticipants = new ArrayList<>();
                participantsContainer.removeAllViews();
                for (DataSnapshot challengeSnapshot : snapshot.getChildren()) {
                    String challengeName = challengeSnapshot.child("name").getValue(String.class);
                    System.out.println("iterating. currently at: " + challengeName);
                    if (challengeName.equals(name)) {
                        System.out.println("found a match! " + challengeName + " equals " + name);

                        for (DataSnapshot participant : challengeSnapshot.child("participants").getChildren()) {
                            System.out.println(participant.getKey());
                            // capture the participantkeys.
                            addParticipantView(participant.getKey(), "accepted");
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
            return; // or handle this case appropriately
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View participantView = inflater.inflate(R.layout.button_community_individual_participant, null);

        TextView participantTextView = participantView.findViewById(R.id.participantsTextView);
        TextView statusTextView = participantView.findViewById(R.id.statusTextView);

        participantTextView.setText(participantID);
        statusTextView.setText(status);

        participantsContainer.addView(participantView);
        listOfParticipants.add(participantView);
    }

}