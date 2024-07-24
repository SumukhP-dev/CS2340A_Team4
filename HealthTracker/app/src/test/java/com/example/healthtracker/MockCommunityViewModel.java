package com.example.healthtracker;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//changes need to be made to
public class MockCommunityViewModel {
    private FakeUser user;

    private String nameErrorMessage;

    private String descriptionErrorMessage;

    private String deadlineErrorMessage;

    private Integer numOfUserChallenges;
    private MockDatabase mDatabase;

    public MockDatabase getDatabase() {
        return mDatabase;
    }

    public String getNameErrorMessage() {
        return nameErrorMessage;
    }

    public void setNameErrorMessage(String nameErrorMessage) {
        this.nameErrorMessage = nameErrorMessage;
    }

    public String getDescriptionErrorMessage() {
        return descriptionErrorMessage;
    }

    public void setDescriptionErrorMessage(String descriptionErrorMessage) {
        this.descriptionErrorMessage = descriptionErrorMessage;
    }

    public String getDeadlineErrorMessage() {
        return deadlineErrorMessage;
    }

    public void setDeadlineErrorMessage(String deadlineErrorMessage) {
        this.deadlineErrorMessage = deadlineErrorMessage;
    }

    public MockCommunityViewModel(FakeUser user, MockDatabase mDatabase) {
        this.user = user;
        nameErrorMessage = null;
        descriptionErrorMessage = null;
        deadlineErrorMessage = null;
        numOfUserChallenges = 0;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public void publishChallenge(String name, String description, String deadline, String username) {
        nameErrorMessage = null;
        descriptionErrorMessage = null;
        deadlineErrorMessage = null;

        boolean valid = true;
        valid = checkForEmptyValues(name, description, deadline);

        if (!valid) {
            return;
        }

        /*DatabaseReference challengeRef = user.getDatabase().getReference("Community");
        challengeRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    addUsernameToCommunity(challengeRef, username);
                }
            }
        });*/
        createChallenge(name, description, deadline, username);
        logNumberOfChallengesForUser(mDatabase, user);
    }

    /*public void addUsernameToCommunity(DatabaseReference challengeRef, String username) {
        challengeRef.child(username).push();
    }overly specific for firebase implementation*/

    public void createChallenge(String name, String description, String deadline, String username) {

            FakeCommunityChallenge newChallenge = new FakeCommunityChallenge(deadline, description, name);
            user.addChallenge(newChallenge);

    }

    public void logNumberOfChallengesForUser(MockDatabase mDatabase, FakeUser user) {
        numOfUserChallenges = user.getChallenges().size();
    }

    public boolean checkForEmptyValues(String name, String description, String deadline) {
        boolean check = true;
        if ((name.length() == 0) || (name.isEmpty())) {
            nameErrorMessage = "Challenge name cannot be empty.";
            check = false;
        }
        if ((description.length() == 0) || (description.isEmpty())) {
            descriptionErrorMessage = "Challenge description cannot be empty.";
            check = false;
        }
        if ((deadline.length() == 0) || (deadline.isEmpty())) {
            deadlineErrorMessage= "Challenge deadline cannot be empty.";
            check = false;
        } else {
            check = check && validateDeadline(deadline);
        }
        return check;
    }

    public boolean validateDeadline(String deadline) {
        // Check if deadline only contains digits & is 8 characters long
        if (!deadline.matches("\\d{8}")) {
            deadlineErrorMessage = "Deadline must be in format YYYYMMDD and contain only digits.";
            return false;
        }

        // Check if deadline is not before the current date
        // - Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);

        try {
            Date deadlineDate = dateFormat.parse(deadline);


            // Check if the deadline is not before the current date
            Calendar currentCalendar = Calendar.getInstance();
            Date currentDate = currentCalendar.getTime();

            if (deadlineDate.before(currentDate)) {
                deadlineErrorMessage = "Deadline may not be before or equal to the current date.";
                return false;
            }

        } catch (ParseException e) {
            deadlineErrorMessage = "Invalid deadline format.";
            return false;
        }
        return true;
    }

    public void removeExpiredChallenges() {
        MockDatabase challengeRef = mDatabase;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);

        for (FakeCommunityChallenge challenge : user.getChallenges()) {
            try {
                Date deadlineDate = dateFormat.parse(challenge.getDeadline());
                Date currentDate = Calendar.getInstance().getTime();

                if (deadlineDate.before(currentDate)) {
                    user.removeChallenge(challenge);
                }
            } catch (ParseException e) {
                deadlineErrorMessage = e.getMessage();
            }
        }

    }
}
