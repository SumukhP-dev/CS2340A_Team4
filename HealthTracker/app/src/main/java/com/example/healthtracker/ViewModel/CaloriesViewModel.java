package com.example.healthtracker.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaloriesViewModel extends ViewModel {

    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<List<Map<String, Object>>> workouts = new MutableLiveData<>();

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<Map<String, Object>>> getWorkouts() {
        return workouts;
    }

    public void fetchUserData(String username) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(username);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                user.setValue(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Can't get user info");
            }
        });
    }
    public void fetchWorkoutData(String userId) {
        DatabaseReference workoutRef = FirebaseDatabase.getInstance().getReference("Workouts").child(userId);
        workoutRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map<String, Object>> workoutList = new ArrayList<>();
                for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> workout = (Map<String, Object>) workoutSnapshot.getValue();
                    workoutList.add(workout);
                }
                workouts.setValue(workoutList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                System.out.println("Can't get workouts info");
            }
        });
    }
}
