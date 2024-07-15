package com.example.healthtracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {
    private MockDatabase mDatabase;
    private FakeUser test1;
    private FakeUser test2;
    private FakeUser test3;
    private MockPersonalInformationViewModel personalInfo;
    private MockSignUpViewModel signUp;
    private MockWorkoutsViewModel workoutsViewModel;

    @Before
    public void setUp() {
        mDatabase = new MockDatabase();
        test1 = new FakeUser("test1", "1234");
        mDatabase.addUser(test1);
        test2 = new FakeUser("test2", "5678");
        mDatabase.addUser(test2);
        test3 = new FakeUser("test3", "abcd");
        mDatabase.addUser(test3);
        signUp = new MockSignUpViewModel();
    }



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
    public void CheckForNegativeSetsOrRepsOrTime1() {
        String SetsString = "-1";
        String RepsString = "-1";
        String TimeString = "-1";
        boolean expectedRes = false;
        boolean actualRes = checkForNegativeSetsOrRepsOrTime(SetsString, RepsString, TimeString);
        assertEquals(expectedRes, actualRes);
    }

    public boolean checkForNegativeSetsOrRepsOrTime(String Sets, String Reps, String Time) {
        boolean check = true;
        double sets=Double.parseDouble(Sets);
        double reps=Double.parseDouble(Reps);
        double time=Double.parseDouble(Time);
        //
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

    public int logNumberOfWorkoutPlansForUser(FakeUser user) {
        int res= user.getCounter();
        return res;
    }







    @Test
    public void testAddWorkout() {
        FakeUser user = new FakeUser("arnava2004", "password");
        String workoutName = "Workout Test";
        FakeWorkout workout = new FakeWorkout("Title", workoutName, "",
                100, 3, 10);
        user.addWorkout(workout);

        FakeWorkout check = user.getWorkout().get(0);
        assertEquals(check, workout);
        assertEquals(1, user.getCounter());
    }

    @Test
    public void testDatabaseUpdate() {
        FakeUser user = new FakeUser("arnava2004", "password");
        String workoutName = "Workout Test";
        FakeWorkout workout = new FakeWorkout("Title", workoutName, "",
                100, 3, 10);
        user.addWorkout(workout);
        mDatabase.addUser(user);
        FakeUser test = mDatabase.getUser(user);
        FakeWorkout testWorkout = test.getWorkout().get(0);

        assertEquals(testWorkout, workout);
        assertEquals(1, user.getCounter());

        mDatabase.removeUser(test);
    }

    @Test
    public void testBlankCalorie() {
        FakeUser user = new FakeUser("arnava2004", "password");
        String workoutName = "Workout Test";
        FakeWorkout workout = new FakeWorkout("Title", workoutName, "",
                0, 3, 10);
        user.addWorkout(workout);
        FakeWorkout check = user.getWorkout().get(0);
        assertEquals(check, workout);
        assertEquals(1, user.getCounter());
    }

    //tests for Personal Information View Model

    //tests for Workouts VeiwModel
    @Test
    public void testInvalidNameWorkoutPlan() {
        FakeUser user = new FakeUser("aranava2004", "password");
        mDatabase.addUser(user);
        workoutsViewModel = new MockWorkoutsViewModel(user, mDatabase);
        workoutsViewModel.publishWorkoutPlan("", "notes", "100", "100", "100", "100", user.getUsername());//reject
        assertEquals(0, user.getWorkoutPlans().size());
    }

    @Test
    public void testPublishWorkoutPlansValid() {
        test1 = new FakeUser("test1", "1234");
        workoutsViewModel = new MockWorkoutsViewModel(test1, mDatabase);
        workoutsViewModel.publishWorkoutPlan("h", "n/a", "1", "5", "2", "9", test1.getUsername());
        assertEquals(1, test1.getWorkoutPlans().size());
    }

    @Test
    public void testCheckForInvalidNameCalories() {
        workoutsViewModel = new MockWorkoutsViewModel(test1, mDatabase);
        boolean valid = workoutsViewModel.checkForEmptyNameOrCalories("", "");
        assertFalse(valid);
    }

    @Test
    public void testCheckForValidNameCalories() {
        workoutsViewModel = new MockWorkoutsViewModel(test1, mDatabase);
        boolean valid = workoutsViewModel.checkForEmptyNameOrCalories("hello", "20");
        assertTrue(valid);
    }

    @Test
    public void testCheckInvalidNameMessage() {
        workoutsViewModel = new MockWorkoutsViewModel(test1, mDatabase);
        workoutsViewModel.checkForEmptyNameOrCalories("", "2");
        String invalidNameMessage = workoutsViewModel.getNameErrorMessage();
        assertEquals(invalidNameMessage, "Name is empty.");
    }

    @Test
    public void testCheckInvalidCaloriesMessage() {
        workoutsViewModel = new MockWorkoutsViewModel(test1, mDatabase);
        workoutsViewModel.checkForEmptyNameOrCalories("h", "");
        String invalidCaloriesMessage = workoutsViewModel.getCaloriesErrorMessage();
        assertEquals(invalidCaloriesMessage, "Calories per set is empty.");
    }
}