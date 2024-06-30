package com.example.healthtracker;

import org.junit.Before;
import org.junit.Test;
import androidx.lifecycle.ViewModelProvider;

import static org.junit.Assert.*;

import com.example.healthtracker.ViewModel.LoginViewModel;
import com.example.healthtracker.ViewModel.PersonalInformationViewModel;

public class PersonalInfoTests {
    private PersonalInformationViewModel pivm;

    @Before
    public void setup() {
        LoginViewModel loginViewModel =new LoginViewModel();
        //using useraccount miranda w/ password CS2340 to test
        loginViewModel.login("miranda", "CS2340");
        pivm = new PersonalInformationViewModel();
    }

    @Test
    public void testGetUsername() {
        assertEquals("miranda", pivm.getUsername());
    }

    @Test
    public void testGetGender() {
        assertEquals("male", pivm.getGender());
    }

    @Test
    public void testAddPersonalData() {
        pivm.addPersonalData("miranda", "Miranda", 200.0, 200.0, "female");
        assertEquals("miranda", pivm.getUsername());
        assertEquals("female", pivm.getGender());
    }

    @Test
    public void testSetGender() {
        pivm.setGender("other");
        pivm.setGender("female");
        assertEquals("female", pivm.getGender());
    }
}
