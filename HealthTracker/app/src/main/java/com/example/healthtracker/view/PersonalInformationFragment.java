package com.example.healthtracker.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.PersonalInformationViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInformationFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private PersonalInformationViewModel personalInformationViewModel;
    private EditText nameTextView;
    private EditText heightEditTextNumberDecimal;
    private EditText weightEditTextNumberDecimal;

    private Button maleButton;
    private Button femaleButton;
    private Button otherButton;
    private Button saveInformationButton;

    public PersonalInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInformationFragment.
     */
    public static PersonalInformationFragment newInstance(String param1, String param2) {
        PersonalInformationFragment fragment = new PersonalInformationFragment();
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
        View view =  inflater.inflate(R.layout.fragment_personal_information, container, false);

        personalInformationViewModel = new ViewModelProvider(this)
                .get(PersonalInformationViewModel.class);

        nameTextView = view.findViewById(R.id.nameEditTextView);
        heightEditTextNumberDecimal = view.findViewById(R.id.heightEditTextNumberDecimal);
        weightEditTextNumberDecimal = view.findViewById(R.id.weightEditTextNumberDecimal);
        maleButton = view.findViewById(R.id.maleButton);
        femaleButton = view.findViewById(R.id.femaleButton);
        otherButton = view.findViewById(R.id.otherButton);
        saveInformationButton = view.findViewById(R.id.saveInformationButton);

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalInformationViewModel.setGender("male");
                maleButton.setBackgroundColor(Color.parseColor("#FF000000"));
                maleButton.setTextColor(Color.parseColor("#FFFFFFFF"));

                femaleButton.setBackgroundColor(Color.parseColor("#D3D3D3"));
                femaleButton.setTextColor(Color.parseColor("#FF000000"));
                otherButton.setBackgroundColor(Color.parseColor("#D3D3D3"));
                otherButton.setTextColor(Color.parseColor("#FF000000"));
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalInformationViewModel.setGender("female");
                femaleButton.setBackgroundColor(Color.parseColor("#FF000000"));
                femaleButton.setTextColor(Color.parseColor("#FFFFFFFF"));

                maleButton.setBackgroundColor(Color.parseColor("#D3D3D3"));
                maleButton.setTextColor(Color.parseColor("#FF000000"));
                otherButton.setBackgroundColor(Color.parseColor("#D3D3D3"));
                otherButton.setTextColor(Color.parseColor("#FF000000"));
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalInformationViewModel.setGender("other");
                otherButton.setBackgroundColor(Color.parseColor("#FF000000"));
                otherButton.setTextColor(Color.parseColor("#FFFFFFFF"));

                maleButton.setBackgroundColor(Color.parseColor("#D3D3D3"));
                maleButton.setTextColor(Color.parseColor("#FF000000"));
                femaleButton.setBackgroundColor(Color.parseColor("#D3D3D3"));
                femaleButton.setTextColor(Color.parseColor("#FF000000"));
            }
        });

        saveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalInformationViewModel.addPersonalData(
                        personalInformationViewModel.getUsername(),
                        nameTextView.getText().toString(),
                        Double.parseDouble(heightEditTextNumberDecimal.getText().toString()),
                        Double.parseDouble(weightEditTextNumberDecimal.getText().toString()),
                        personalInformationViewModel.getGender());
            }
        });

        return view;
    }
}




