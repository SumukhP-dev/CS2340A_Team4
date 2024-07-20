package com.example.healthtracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.healthtracker.R;

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
        back = constraintLayout.findViewById(R.id.communityBackButton);

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
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}