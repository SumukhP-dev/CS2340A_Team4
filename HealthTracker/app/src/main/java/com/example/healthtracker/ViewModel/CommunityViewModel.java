package com.example.healthtracker.ViewModel;

import android.util.Log;

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

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityViewModel extends ViewModel {
    private User user;
    private ArrayList<String> workoutPlans;
    private ArrayList<String> participants;
    private MutableLiveData<Integer> numOfUserChallenges;
    private MutableLiveData<Boolean> duplicate;

    private MutableLiveData<String> nameErrorMessage;
    private MutableLiveData<String> descriptionErrorMessage;
    private MutableLiveData<String> deadlineErrorMessage;
    private MutableLiveData<String> workoutPlansMinimumErrorMessage;

    public CommunityViewModel() {
        user = User.getInstance();
        nameErrorMessage = new MutableLiveData<>(null);
        descriptionErrorMessage = new MutableLiveData<>(null);
        deadlineErrorMessage = new MutableLiveData<>(null);
        workoutPlansMinimumErrorMessage = new MutableLiveData<>(null);
        workoutPlans = new ArrayList<>();
        participants = new ArrayList<>();
        numOfUserChallenges = new MutableLiveData<>(0);
        duplicate = new MutableLiveData<>(false);
    }

    public boolean getDuplicate() {
        return Boolean.TRUE.equals(duplicate.getValue());
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate.setValue(duplicate);
    }

    public FirebaseDatabase getDatabase() {
        return user.getDatabase();
    }

    public String getWorkoutPlansMinimumErrorMessage() {
        return workoutPlansMinimumErrorMessage.getValue();
    }

    public void setWorkoutPlansMinimumErrorMessage(String workoutPlansMinimumErrorMessage) {
        this.workoutPlansMinimumErrorMessage.setValue(workoutPlansMinimumErrorMessage);
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

    public void addToParticipantsArrayList(String participants) {
        this.participants.add(participants);
    }

    public void clearWorkoutPlanArrayList() {
        workoutPlans = new ArrayList<>();
    }

    public void clearParticipantsArrayList() {
        participants = new ArrayList<>();
    }

    public String getDescriptionErrorMessage () {
        return descriptionErrorMessage.getValue();
    }

    public void setDescriptionErrorMessage (String descriptionErrorMessage){
        this.descriptionErrorMessage.setValue(descriptionErrorMessage);
    }

    public String getDeadlineErrorMessage() {
        return deadlineErrorMessage.getValue();
    }

    public void setDeadlineErrorMessage (String deadlineErrorMessage){
        this.deadlineErrorMessage.setValue(deadlineErrorMessage);
    }

        public String getUsername() {
            return user.getUsername();
        }

        public void setUsername (String username){
            user.setUsername(username);
        }

        public void publishChallenge (String name, String description, String deadline, String
        username){
            nameErrorMessage = new MutableLiveData<>(null);
            descriptionErrorMessage = new MutableLiveData<>(null);
            deadlineErrorMessage = new MutableLiveData<>(null);

            boolean valid = true;
            valid = checkForEmptyValues(name, description, deadline);

            if (!checkForMinimumWorkoutPlans()) {
                return;
            }

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

        public void addUsernameToCommunity (DatabaseReference challengeRef, String username){
            challengeRef.child(username).push();
        }

        public void createChallenge (String name, String description, String deadline,
                                     String username, ArrayList<String> workoutPlans){

            DatabaseReference challengeRef = user.getDatabase().getReference("Community");
            challengeRef.child(username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean noDuplicates = true;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (name.equals(postSnapshot.child("name").getValue())) {
                            noDuplicates = false;
                            break;
                        }
                    }

                    if (noDuplicates) {
                        logNumberOfChallengesForUser(challengeRef, username);

                        String challengeNumber = "Challenge " + (numOfUserChallenges.getValue() + 1);
                        challengeRef.child(username).child(challengeNumber).push();

                        DatabaseReference ref = challengeRef.child(username).child(challengeNumber);

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("name", name);
                        childUpdates.put("description", description);
                        childUpdates.put("deadline", deadline);
                        childUpdates.put("workoutPlans", workoutPlans);

                        ref.updateChildren(childUpdates);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void logNumberOfChallengesForUser (DatabaseReference challengeRef, String username){
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

        public boolean checkForEmptyValues (String name, String description, String deadline){
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
            } else {
                check = check && validateDeadline(deadline);
            }
            return check;
        }

        public boolean validateDeadline (String deadline){
            // Check if deadline is not before the current date
            // - Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            dateFormat.setLenient(false);

            try {
                Date deadlineDate = dateFormat.parse(deadline);

                // Check if the deadline is not before the current date
                Calendar currentCalendar = Calendar.getInstance();
                Date currentDate = currentCalendar.getTime();

                if (deadlineDate.before(currentDate)) {
                    deadlineErrorMessage.setValue("Deadline may not be before or equal to the current date.");
                    return false;
                }

            } catch (ParseException e) {
                deadlineErrorMessage.setValue("Invalid deadline format.");
                return false;
            }
            return true;
        }

        public void removeExpiredChallenges() {
            DatabaseReference challengeRef = user.getDatabase().getReference("Community");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            dateFormat.setLenient(false);

            challengeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot challengeSnapshot : userSnapshot.getChildren()) {
                            String deadline = challengeSnapshot.child("deadline").getValue(String.class);
                            if (deadline != null) {
                                try {
                                    Date deadlineDate = dateFormat.parse(deadline);
                                    Date currentDate = Calendar.getInstance().getTime();

                                    if (deadlineDate.before(currentDate)) {
                                        challengeSnapshot.getRef().removeValue();
                                        Log.d("RemoveExpiredChallenges", "Removed challenge: " + challengeSnapshot.getKey());
                                    }
                                } catch (ParseException e) {
                                    Log.e("RemoveExpiredChallenges", "Error parsing date: " + deadline, e);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public boolean checkForMinimumWorkoutPlans() {
            if(workoutPlans.isEmpty()) {
                workoutPlansMinimumErrorMessage.setValue("Must add at least 1 workout plan.");
                return false;
            }
            return true;
        }
}
