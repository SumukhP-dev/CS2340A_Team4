package com.example.healthtracker;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.annotation.NonNull;

import com.example.healthtracker.model.User;
import com.example.healthtracker.view.CaloriesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

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


}