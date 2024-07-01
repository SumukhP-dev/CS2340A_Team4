package com.example.healthtracker;

import org.junit.Before;
import org.junit.Test;
//import org.junit.runner.RunWith
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

import com.example.healthtracker.ViewModel.LoginViewModel;
import com.example.healthtracker.ViewModel.PersonalInformationViewModel;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.errorprone.annotations.DoNotMock;

//@RunWith(MockitoJUnitRunner.class)
public class PersonalInformationTests {
    //@Mock
    private PersonalInformationViewModel pivm;
    //@Mock
    private LoginViewModel loginViewModel;

    @Before
    public void setup() {
        loginViewModel =new LoginViewModel();
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
        boolean success = true;
        try {
            pivm.addPersonalData("miranda", "Miranda", 200.0, 200.0, "female");
        } catch(Exception error) {
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void testSetGender() {
        pivm.setGender("other");
        pivm.setGender("female");
        assertEquals("female", pivm.getGender());
    }
}
