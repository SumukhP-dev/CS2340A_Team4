package com.example.healthtracker.view;

import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public interface Strategy {
    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons);
}
