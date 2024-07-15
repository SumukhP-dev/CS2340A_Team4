package com.example.healthtracker.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PersonalInformationViewModel extends ViewModel {
    private User user;
    private MutableLiveData<String> gender;

    private DatabaseReference mDatabase;

    public PersonalInformationViewModel() {
        user = User.getInstance();
        gender = new MutableLiveData<String>("male");
    }

    public String getGender() {
        return gender.getValue();
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    // Updates personal information or creates a
    // document and enters said personal information in database
    public void addPersonalData(String username, String name, Double height, Double weight,
                                String gender) {
        DatabaseReference userRef = user.getDatabase().getReference("User");
        userRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue() == null) {
                    createUserDocument(userRef, username, name, height, weight, gender);
                } else {
                    updateDocument(userRef, username, name, height, weight, gender);
                }
            }
        });
    }

    // Creates a new user with name, height, weight, gender, and username
    public void createUserDocument(DatabaseReference userRef, String username, String name,
                                    Double height, Double weight, String gender) {
        userRef.child(username).push();
        DatabaseReference ref = userRef.child(username);

        ref.setValue("name");
        ref.child("name").setValue(name);

        ref.setValue("height");
        ref.child("height").setValue(height);

        ref.setValue("weight");
        ref.child("weight").setValue(weight);

        ref.setValue("gender");
        ref.child("gender").setValue(gender);

        ref.setValue("Counter");
        ref.child("Counter").setValue("0");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Workouts").setValue(username);
    }

    // Updates personal information of user
    public void updateDocument(DatabaseReference userRef, String username, String name,
                                Double height, Double weight, String gender) {
        DatabaseReference ref = userRef.child(username);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("name", name);
        childUpdates.put("height", height);
        childUpdates.put("weight", weight);
        childUpdates.put("gender", gender);

        ref.updateChildren(childUpdates);
    }
}
