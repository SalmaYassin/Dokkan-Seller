package com.example.dokkanseller.views;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.dokkanseller.R;
import com.example.dokkanseller.SharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Integer> list = new ArrayList<>();
        list.add(R.id.welcomPage);
        list.add(R.id.register);
        list.add(R.id.step1);
        list.add(R.id.step2);
        list.add(R.id.login);
        list.add(R.id.forgetPassword);


        if (SharedPreference.getInstance(this).isUserSaved()) {
            Navigation.findNavController(this, R.id.my_nav_host).navigate(R.id.action_welcomPage_to_homeFragment2);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.my_nav_host);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (list.contains(destination.getId())) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }


            }
        });


    }

}



