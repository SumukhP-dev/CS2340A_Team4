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









    @Test
    public void testDatabaseUpdate() {
        FakeUser user = new FakeUser("arnava2004", "password");
        String workoutName = "Workout Test";
        FakeWorkout workout = new FakeWorkout("Title", workoutName, "",
                100, 3, 10);
        user.addWorkout(workout);

        FakeWorkout check = user.getWorkout().get(0);
        assertEquals(check, workout);
        assertEquals(1, user.getCounter());
    }
}