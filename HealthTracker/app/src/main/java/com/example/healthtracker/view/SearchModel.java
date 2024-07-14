package com.example.healthtracker.view;

import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SearchModel {
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void remove(LinearLayout container, String query, ArrayList<Button> listOfButtons) {
        strategy.remove(container, query, listOfButtons);
    }
}
