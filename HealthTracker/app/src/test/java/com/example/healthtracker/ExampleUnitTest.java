package com.example.healthtracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.healthtracker.view.CaloriesFragment;
import com.github.mikephil.charting.data.PieEntry;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void caloriesGoalMenIsCorrect() {
        CaloriesFragment test = new CaloriesFragment();
        double menGoal;
        menGoal = test.goalMen(100, 170, 30);
        assertEquals(1917.5, menGoal, 0.0001);
    }

    @Test
    public void  caloriesGoalWomenIsCorrect() {
        CaloriesFragment test = new CaloriesFragment();
        double womenGoal;
        womenGoal = test.goalWomen(100, 170, 30);
        assertEquals(1751.5, womenGoal, 0.0001);
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
    public void testPieEntriesCreationNegativeCalories() {
        CaloriesFragment fragment = new CaloriesFragment();
        String curCalories = "-300";
        double goalCalories = 500;

        // Call getPieEntries
        List<PieEntry> entries = fragment.getPieEntries(curCalories, goalCalories);

        // Verify data processing logic
        assertNotNull(entries);
        assertEquals(2, entries.size());
        assertEquals(-300f, entries.get(0).getValue(), 0.001);
        assertEquals("Current burning", entries.get(0).getLabel());
        assertEquals(500f, entries.get(1).getValue(), 0.001);
        assertEquals("Goal", entries.get(1).getLabel());
    }

    @Test
    public void testGoalCalculationAndPieEntryCreation() {
        CaloriesFragment fragment = new CaloriesFragment();
        double weight = 100;
        double height = 170;
        double age = 30;

        // Calculate goal for men
        double goalMen = fragment.goalMen(weight, height, age);

        // Set current calories
        String curCalories = "300";

        // Call getPieEntries
        List<PieEntry> entries = fragment.getPieEntries(curCalories, goalMen);

        // Verify data processing logic
        assertNotNull(entries);
        assertEquals(2, entries.size());
        assertEquals(300f, entries.get(0).getValue(), 0.001);
        assertEquals("Current burning", entries.get(0).getLabel());
        assertEquals((float) goalMen, entries.get(1).getValue(), 0.001);
        assertEquals("Goal", entries.get(1).getLabel());
    }

    @Test
    public void testGoalCalculationWithLowValues() {
        CaloriesFragment fragment = new CaloriesFragment();
        double weight = 30;
        double height = 100;
        double age = 10;

        // Calculate goal for men with low values
        double goalMen = fragment.goalMen(weight, height, age);
        assertTrue(goalMen > 0);

        // Calculate goal for women with low values
        double goalWomen = fragment.goalWomen(weight, height, age);
        assertTrue(goalWomen > 0);
    }

    @Test
    public void testCleanUsernameMethod1() {
        CaloriesFragment fragment = new CaloriesFragment();
        String cleanUsername =
                fragment.cleanUsername("some@gmail.com");
        assertEquals("some", cleanUsername);
    }

    @Test
    public void testCleanUsernameMethod2() {
        CaloriesFragment fragment = new CaloriesFragment();
        String cleanUsername =
                fragment.cleanUsername("g@gmail.com");
        assertEquals("g", cleanUsername);
    }
    @Test
    public void testCleanUsernameMethod3() {
        CaloriesFragment fragment = new CaloriesFragment();
        String cleanUsername =
                fragment.cleanUsername("so.me.guy@gmail.com");
        assertEquals("someguy", cleanUsername);
    }
    @Test
    public void testCleanUsernameMethod4() {
        CaloriesFragment fragment = new CaloriesFragment();
        String cleanUsername =
                fragment.cleanUsername(
                        "someguysemailcreatednow@gmail.com");
        assertEquals("someguysemailcreatednow", cleanUsername);
    }
}