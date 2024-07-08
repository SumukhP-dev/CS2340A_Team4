package com.example.healthtracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;

import com.example.healthtracker.view.CaloriesFragment;
import com.example.healthtracker.view.TrackerFragment;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {
    private DatabaseReference mDatabase;



    // Tests that the entries to the pie chart are created correctly


    @Test
    public void testStringToDoubleConversion1() {
        String calorieString = "500.5";
        double expectedCalories = 500.5;
        double actualCalories = Double.parseDouble(calorieString);
        assertEquals(expectedCalories, actualCalories, 0.0001);
    }

    @Test
    public void testStringToDoubleConversion2() {
        String calorieString = "0.000";
        double expectedCalories = 0.000;
        double actualCalories = Double.parseDouble(calorieString);
        assertEquals(expectedCalories, actualCalories, 0.0001);
    }










    public void trackerDatabaseUpdate() {
        TrackerFragment test = new TrackerFragment();
        String workoutName;
        String counter;
        String username = "TestName";
        Map<String, Object> testUser = new HashMap<>();
        testUser.put("additionalNotes", "");
        testUser.put("caloriesBurned", "100");
        testUser.put("reps", "10");
        testUser.put("sets", "3");
        testUser.put("workoutName", "push");
        mDatabase.child("User").child(username).setValue("TestingCase");
        mDatabase.child("Workouts").child(username)
                .child("TestWorkout").setValue(testUser);
        mDatabase.child("Workout").child(username).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnap = task.getResult();
                        String additionalNotes = String.valueOf(dataSnap
                                .child("TestWorkout")
                                .child("additionalNotes").getValue());
                        assertEquals("", String.valueOf(additionalNotes));
                        String calories = String.valueOf(dataSnap
                                .child("TestWorkout")
                                .child("caloriesBurned").getValue());
                        assertEquals("100", calories);
                        String reps = String.valueOf(dataSnap
                                .child("TestWorkout")
                                .child("reps").getValue());
                        assertEquals("10", reps);
                        String sets = String.valueOf(dataSnap
                                .child("TestWorkout")
                                .child("sets").getValue());
                        assertEquals("3", sets);
                        String workoutName = String.valueOf(dataSnap
                                .child("TestWorkout")
                                .child("workoutName").getValue());
                        assertEquals("push", workoutName);
                    }
                });

        mDatabase.child("User").child(username).removeValue();
        mDatabase.child("Workouts").child(username).child("TestWorkout").removeValue();
    }
}