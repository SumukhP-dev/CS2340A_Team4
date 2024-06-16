package com.example.healthtracker.view;

import android.os.Bundle;

import com.example.healthtracker.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtracker.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    //private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceCurrFragment(new HomeFragment());

        //listens to the navigation bar and sets the fragment corresponding to the icon clicked
        binding.bottomNavigationBar.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home) {
                replaceCurrFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.community) {
                replaceCurrFragment(new CommunityFragment());
            } else if (item.getItemId() == R.id.calories) {
                replaceCurrFragment(new CaloriesFragment());
            } else if (item.getItemId() == R.id.tracker) {
                replaceCurrFragment(new TrackerFragment());
            } else if (item.getItemId() == R.id.workouts) {
                replaceCurrFragment(new WorkoutsFragment());
            }

            return true;
        });
    }

    //replaces current fragment with fragment that was passed in
    private void replaceCurrFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.pageFragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}