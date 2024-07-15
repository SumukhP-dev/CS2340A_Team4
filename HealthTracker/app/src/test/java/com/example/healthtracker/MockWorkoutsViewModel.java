package com.example.healthtracker;

import java.util.HashMap;
import java.util.Map;

public class MockWorkoutsViewModel {
    private FakeUser user;
    private String nameErrorMessage;
    private String caloriesErrorMessage;
    private int numOfUserWorkoutPlans;
    private MockDatabase database;

    public String getNameErrorMessage() {
        return nameErrorMessage;
    }

    public void setNameErrorMessage(String nameErrorMessage) {
        this.nameErrorMessage = nameErrorMessage;
    }

    public String getCaloriesErrorMessage() {
        return caloriesErrorMessage;
    }

    public void setCaloriesErrorMessage(String caloriesErrorMessage) {
        this.caloriesErrorMessage = caloriesErrorMessage;
    }

    public MockWorkoutsViewModel(FakeUser user, MockDatabase database) {
        this.user = user;
        this.database = database;
        nameErrorMessage = null;
        caloriesErrorMessage = null;
        numOfUserWorkoutPlans = 0;
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
        nameErrorMessage = null;
        caloriesErrorMessage = null;
        boolean valid = true;
        valid = checkForEmptyNameOrCalories(name, expectedCalories);

        if (!valid) {
            return;
        }

        if (database.getUser(user) != null) {
            createWorkoutPlan(database, name, notes, sets, reps, time,
                    expectedCalories, username);
        }
    }

    /*This method has been commented out because it does not comply with MockDatabase set up
    public void addUsernameToWorkoutsPlan(DatabaseReference workoutPlanRef, String username) {
        workoutPlanRef.child(username).push();
    }*/


    public void createWorkoutPlan(MockDatabase workoutPlanRef,
                                  String name, String notes, String sets,
                                  String reps, String time,
                                  String expectedCalories,
                                  String username) {
        String title = "WorkoutPlan " + user.getCounter();
        FakeWorkoutPlan workoutPlan = new FakeWorkoutPlan(title, name, notes, time, expectedCalories, sets, reps);
        user.addWorkoutPlan(workoutPlan);
    }

    /*public void logNumberOfWorkoutPlansForUser(DatabaseReference workoutPlanRef,
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
    }*/

    public boolean checkForEmptyNameOrCalories(String name, String expectedCalories) {
        boolean check = true;
        if ((name.length() == 0) || (name.isEmpty())) {
            nameErrorMessage = "Name is empty.";
            check = false;
        }
        if ((expectedCalories.length() == 0) || (expectedCalories.isEmpty())) {
            caloriesErrorMessage = "Calories per set is empty.";
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
