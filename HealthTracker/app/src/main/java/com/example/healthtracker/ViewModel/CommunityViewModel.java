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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommunityViewModel extends ViewModel {
    private User user;
    private MutableLiveData<String> nameErrorMessage;
    private ArrayList<String> workoutPlans;
    private MutableLiveData<Integer> numOfUserChallenges;
    private MutableLiveData<Boolean> completed;

    public CommunityViewModel() {
        user = User.getInstance();
        completed = new MutableLiveData<>(false);
        nameErrorMessage = new MutableLiveData<>(null);
        workoutPlans = new ArrayList<>();
        numOfUserChallenges = new MutableLiveData<>(0);
    }

    public boolean getCompleted() {
        return completed.getValue();
    }

    public void setCompleted(boolean completed) {
        this.completed.setValue(completed);
    }

    public FirebaseDatabase getDatabase() {
        return user.getDatabase();
    }

    public String getNameErrorMessage() {
        return nameErrorMessage.getValue();
    }

    public void setNameErrorMessage(String nameErrorMessage) {
        this.nameErrorMessage.setValue(nameErrorMessage);
    }

    public void addToWorkoutPlanArrayList(String workoutPlan) {
        this.workoutPlans.add(workoutPlan);
    }

    public void clearWorkoutPlanArrayList() {
        workoutPlans = new ArrayList<>();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public void publishChallenge(String name, String description, String deadline, String username) {
        nameErrorMessage = new MutableLiveData<>(null);

        boolean valid = true;
        valid = checkForEmptyName(name);

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
        createChallenge(name, description, deadline, username, workoutPlans);
    }

    public void addUsernameToCommunity(DatabaseReference challengeRef, String username) {
        challengeRef.child(username).push();
    }

    public void createChallenge(String name, String description, String deadline,
                                String username, ArrayList<String> workoutPlans) {

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
                    childUpdates.put("workoutPlans", workoutPlans);
                    childUpdates.put("completed", completed);

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

    public boolean checkForEmptyName(String name) {
        boolean check = true;
        if ((name.length() == 0) || (name.isEmpty())) {
            nameErrorMessage.setValue("Challenge name cannot be empty.");
            check = false;
        }
        return check;
    }
}
