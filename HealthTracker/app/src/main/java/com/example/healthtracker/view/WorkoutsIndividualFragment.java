package com.example.healthtracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.healthtracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutsIndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutsIndividualFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private ConstraintLayout constraintLayout;

    private TextView setAuthor;
    private TextView setSets;
    private TextView setReps;
    private TextView setTime;
    private TextView setNotes;
    private TextView setCals;
    private TextView setName;

    private ImageButton back;


    public WorkoutsIndividualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutsIndividualFragment.
     */
    public static WorkoutsIndividualFragment newInstance(String param1, String param2) {
        WorkoutsIndividualFragment fragment = new WorkoutsIndividualFragment();
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
        View view = inflater.inflate(R.layout.fragment_workouts_individual, container, false);

        constraintLayout = view.findViewById(R.id.frameLayout3);

        setAuthor = constraintLayout.findViewById(R.id.authorDataTextView);
        setSets = constraintLayout.findViewById(R.id.setsDataTextView);
        setReps = constraintLayout.findViewById(R.id.repsDataTextView);
        setTime = constraintLayout.findViewById(R.id.timeDataTextView);
        setCals = constraintLayout.findViewById(R.id.caloriesDataTextView);
        setNotes = constraintLayout.findViewById(R.id.notesDataTextView);
        setName = constraintLayout.findViewById(R.id.workoutPlanTitleTextView);
        back = constraintLayout.findViewById(R.id.imageButton3);

        Bundle args = getArguments();
        if (args != null) {
            String userId = args.getString("userId");
            String cals = args.getString("expectedCalories");
            String name = args.getString("name");
            String notes = args.getString("notes");
            String reps = args.getString("reps");
            String sets = args.getString("sets");
            String time = args.getString("time");

            setAuthor.setText(userId);
            setSets.setText(sets);
            setReps.setText(reps);
            setTime.setText(time);
            setNotes.setText(notes);
            setCals.setText(cals);
            setName.setText(name);

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