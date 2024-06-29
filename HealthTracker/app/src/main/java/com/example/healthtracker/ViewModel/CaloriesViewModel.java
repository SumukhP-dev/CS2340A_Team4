package com.example.healthtracker.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;

import java.util.List;
import java.util.Map;

public class CaloriesViewModel extends ViewModel {

    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<List<Map<String, Object>>> workouts = new MutableLiveData<>();

}
