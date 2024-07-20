package com.example.healthtracker.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CommunityViewModel extends ViewModel {

    private User user;

    private MutableLiveData<String> nameErrorMessage;

    private MutableLiveData<String> descriptionErrorMessage;

    private MutableLiveData<String> deadlineErrorMessage;

    private MutableLiveData<Integer> numOfUserChallenges;

    // private String currentChallenge;

    public FirebaseDatabase getDatabase() {
        return user.getDatabase();
    }

    public String getNameErrorMessage() {
        return nameErrorMessage.getValue();
    }

    public void setNameErrorMessage(String nameErrorMessage) {
        this.nameErrorMessage.setValue(nameErrorMessage);
    }

    public String getDescriptionErrorMessage() {
        return descriptionErrorMessage.getValue();
    }

    public void setDescriptionErrorMessage(String descriptionErrorMessage) {
        this.descriptionErrorMessage.setValue(descriptionErrorMessage);
    }

    public String getDeadlineErrorMessage() {
        return deadlineErrorMessage.getValue();
    }

    public void setDeadlineErrorMessage(String deadlineErrorMessage) {
        this.deadlineErrorMessage.setValue(deadlineErrorMessage);
    }

    public CommunityViewModel() {
        user = User.getInstance();
        nameErrorMessage = new MutableLiveData<>(null);
        descriptionErrorMessage = new MutableLiveData<>(null);
        deadlineErrorMessage = new MutableLiveData<>(null);
        numOfUserChallenges = new MutableLiveData<>(0);
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public void publishChallenge(String name, String description, String deadline, String username) {
        nameErrorMessage = new MutableLiveData<>(null);
        descriptionErrorMessage = new MutableLiveData<>(null);
        deadlineErrorMessage = new MutableLiveData<>(null);

        boolean valid = true;
        valid = checkForEmptyValues(name, description, deadline);

        if (!valid) {
            return;
        }

        DatabaseReference challengeRef = user.getDatabase().getReference("Community");
        challengeRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    addUsernameToCommunity(challengeRef, username);
                }
            }
        });
        createChallenge(name, description, deadline, username);
    }

    public void addUsernameToCommunity(DatabaseReference challengeRef, String username) {
        challengeRef.child(username).push();
    }

    public void createChallenge(String name, String description, String deadline, String username) {

        DatabaseReference challengeRef = user.getDatabase().getReference("Community");
        challengeRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean noDuplicates = true;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (name.equals(postSnapshot.child("name").getValue())) {
                        noDuplicates = false;
                        break;
                    }
                }

                if (noDuplicates) {
                    logNumberOfChallengesForUser(challengeRef, username);

                    String challengeNumber = "Challenge " + numOfUserChallenges.getValue();
                    challengeRef.child(username).child(challengeNumber).push();

                    DatabaseReference ref = challengeRef.child(username).child(challengeNumber);

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("name", name);
                    childUpdates.put("description", description);
                    childUpdates.put("deadline", deadline);

                    ref.updateChildren(childUpdates);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void logNumberOfChallengesForUser(DatabaseReference challengeRef, String username) {
        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numOfUserChallenges.setValue((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        challengeRef.child(username).addValueEventListener(challengeListener);
    }

    public boolean checkForEmptyValues(String name, String description, String deadline) {
        boolean check = true;
        if ((name.length() == 0) || (name.isEmpty())) {
            nameErrorMessage.setValue("Challenge name cannot be empty.");
            check = false;
        }
        if ((description.length() == 0) || (description.isEmpty())) {
            descriptionErrorMessage.setValue("Challenge description cannot be empty.");
            check = false;
        }
        if ((deadline.length() == 0) || (deadline.isEmpty())) {
            deadlineErrorMessage.setValue("Challenge deadline cannot be empty.");
            check = false;
        }
        return check;
    }

}
