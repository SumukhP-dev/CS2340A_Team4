package com.example.healthtracker;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.annotation.NonNull;

import com.example.healthtracker.model.User;
import com.example.healthtracker.view.CaloriesFragment;
import com.example.healthtracker.view.TrackerFragment;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Test
    public void CaloriesGoalMen_isCorrect(){
        CaloriesFragment test= new CaloriesFragment();
        double menGoal;
        menGoal=test.goalMen(100,170,30);
        assertEquals(1917.5, menGoal,0.0001);
    }

    @Test
    public void  CaloriesGoalWomen_isCorrect(){
        CaloriesFragment test= new CaloriesFragment();
        double womenGoal;
        womenGoal=test.goalWomen(100,170,30);
        assertEquals(1751.5, womenGoal,0.0001);
    }

    // Tests that the entries to the pie chart are created correctly
    @Test
    public void testPieEntriesCreation() {
        CaloriesFragment fragment = new CaloriesFragment();
        String curCalories = "300";
        double goalCalories = 500;

        List<PieEntry> entries = fragment.getPieEntries(curCalories, goalCalories);

        assertNotNull(entries);
        assertEquals(2, entries.size());
        assertEquals(300f, entries.get(0).getValue(), 0.001);
        assertEquals("Current burning", entries.get(0).getLabel());
        assertEquals(500f, entries.get(1).getValue(), 0.001);
        assertEquals("Goal", entries.get(1).getLabel());
    }

    @Test
    public void testStringToDoubleConversion() {
        String calorieString = "500.5";
        double expectedCalories = 500.5;
        double actualCalories = Double.parseDouble(calorieString);
        assertEquals(expectedCalories, actualCalories, 0.0001);
    }

    @Test
    public void TrackerDatabaseUpdate(){
        TrackerFragment test= new TrackerFragment();
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
        mDatabase.child("Workouts").child(username).child("TestWorkout").setValue(testUser);
        mDatabase.child("Workout").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnap = task.getResult();
                String additionalNotes = String.valueOf(dataSnap.child("TestWorkout").child("additionalNotes").getValue());
                assertEquals("", String.valueOf(additionalNotes));
                String calories = String.valueOf(dataSnap.child("TestWorkout").child("caloriesBurned").getValue());
                assertEquals("100", calories);
                String reps = String.valueOf(dataSnap.child("TestWorkout").child("reps").getValue());
                assertEquals("10", reps);
                String sets = String.valueOf(dataSnap.child("TestWorkout").child("sets").getValue());
                assertEquals("3", sets);
                String workoutName = String.valueOf(dataSnap.child("TestWorkout").child("workoutName").getValue());
                assertEquals("push", workoutName);
            }
        });

        mDatabase.child("User").child(username).removeValue();
        mDatabase.child("Workouts").child(username).child("TestWorkout").removeValue();
    }

}