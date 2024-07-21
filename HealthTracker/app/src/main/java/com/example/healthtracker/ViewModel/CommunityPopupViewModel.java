package com.example.healthtracker.ViewModel;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CommunityPopupViewModel extends ViewModel {
    private User user;
    private MutableLiveData<Integer> numOfUserWorkoutPlans;

    public CommunityPopupViewModel() {
        user = User.getInstance();
        numOfUserWorkoutPlans = new MutableLiveData<Integer>(0);
    }

    public void publishWorkoutPlan(String name, String notes,
                                   String sets,
                                   String reps, String time,
                                   String expectedCalories,
                                   String username) {
        DatabaseReference workoutPlanRef = user.getDatabase().getReference("WorkoutPlans");
        workoutPlanRef.child(username).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            addUsernameToWorkoutsPlan(workoutPlanRef, username);
                        }
                    }
                });

        createWorkoutPlan(name, notes, sets, reps, time,
                expectedCalories, username);
    }

    public void addUsernameToWorkoutsPlan(DatabaseReference workoutPlanRef, String username) {
        workoutPlanRef.child(username).push();
    }


    public void createWorkoutPlan(String name, String notes, String sets,
                                  String reps, String time,
                                  String expectedCalories,
                                  String username) {
        DatabaseReference workoutPlanRef = user.getDatabase().getReference("WorkoutPlans");
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
}
