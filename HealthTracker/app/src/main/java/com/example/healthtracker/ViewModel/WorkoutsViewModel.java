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
import java.util.HashMap;
import java.util.Map;

public class WorkoutsViewModel extends ViewModel {
    private User user;
    private MutableLiveData<String> nameErrorMessage;
    private MutableLiveData<String> caloriesErrorMessage;
    private MutableLiveData<Integer> numOfUserWorkoutPlans;

    public String getNameErrorMessage() {
        return nameErrorMessage.getValue();
    }

    public void setNameErrorMessage(String nameErrorMessage) {
        this.nameErrorMessage.setValue(nameErrorMessage);
    }

    public String getCaloriesErrorMessage() {
        return caloriesErrorMessage.getValue();
    }

    public void setCaloriesErrorMessage(String caloriesErrorMessage) {
        this.caloriesErrorMessage.setValue(caloriesErrorMessage);
    }

    public WorkoutsViewModel() {
        user = User.getInstance();
        nameErrorMessage = new MutableLiveData<String>(null);
        caloriesErrorMessage = new MutableLiveData<String>(null);
        numOfUserWorkoutPlans = new MutableLiveData<Integer>(0);
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }


    public void publishWorkoutPlan(String name, String notes,
                                      String sets,
                                      String reps, String time,
                                      String expectedCalories,
                                      String username) {
        nameErrorMessage = new MutableLiveData<String>(null);
        caloriesErrorMessage = new MutableLiveData<String>(null);
        boolean valid = true;
        valid = checkForEmptyNameOrCalories(name, expectedCalories);

        if (!valid) {
            return;
        }

        DatabaseReference workoutPlanRef = user.getDatabase().getReference("WorkoutPlans");
        workoutPlanRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    addUsernameToWorkoutsPlan(workoutPlanRef, username);
                }
            }
        });

        createWorkoutPlan(workoutPlanRef, name, notes, sets, reps, time,
                expectedCalories, username);
    }

    public void addUsernameToWorkoutsPlan(DatabaseReference workoutPlanRef, String username) {
        workoutPlanRef.child(username).push();
    }


    public void createWorkoutPlan(DatabaseReference workoutPlanRef,
                                       String name, String notes, String sets,
                                       String reps, String time,
                                       String expectedCalories,
                                       String username) {
        workoutPlanRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean noDuplicates = true;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (name.equals(postSnapshot.child("name").getValue())) {
                        noDuplicates = false;
                        break;
                    }
                }
                if (noDuplicates) {
                    logNumberOfWorkoutPlansForUser(workoutPlanRef, username);

                    String workoutPlanNumber = "WorkoutPlan " + numOfUserWorkoutPlans.getValue();
                    workoutPlanRef.child(username).child(workoutPlanNumber).push();
                    DatabaseReference ref = workoutPlanRef.child(username).child(workoutPlanNumber);

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("name", name);
                    childUpdates.put("notes", notes);
                    childUpdates.put("sets", sets);
                    childUpdates.put("reps", reps);
                    childUpdates.put("time", time);
                    childUpdates.put("expectedCalories", expectedCalories);

                    ref.updateChildren(childUpdates);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void logNumberOfWorkoutPlansForUser(DatabaseReference workoutPlanRef,
                                               String username) {
        ValueEventListener workoutPlanListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numOfUserWorkoutPlans.setValue((int) dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        workoutPlanRef.child(username).addValueEventListener(workoutPlanListener);
    }

    public boolean checkForEmptyNameOrCalories(String name, String expectedCalories) {
        boolean check = true;
        if ((name.length() == 0) || (name.isEmpty())) {
            nameErrorMessage.setValue("Name is empty.");
            check = false;
        }
        if ((expectedCalories.length() == 0) || (expectedCalories.isEmpty())) {
            caloriesErrorMessage.setValue("Calories per set is empty.");
            check = false;
        }
        return check;
    }

    public boolean checkForNegativeSetsOrRepsOrTime(String Sets, String Reps, String Time) {
        boolean check = true;
        double sets=Double.parseDouble(Sets);
        double reps=Double.parseDouble(Reps);
        double time=Double.parseDouble(Time);
        if (sets<0) {
            check = false;
        }
        if (reps<0) {
            check = false;
        }
        if (time<0) {

            check = false;
        }
        return check;
    }
}