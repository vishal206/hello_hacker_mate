package com.example.hellohackermate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashBoardHacker extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String myuid;
    ActionBar actionBar;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_hacker);

        actionBar =getSupportActionBar();
        actionBar.setTitle("Profile Activity");

        navigationView =findViewById(R.id.navigationHacker);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        actionBar.setTitle("Home");

//        HomeFragment fragment=new HomeFragment();
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.content,fragment,"");
//        fragmentTransaction.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.navhacker_home:
                    actionBar.setTitle("Home");
                    HomeFragment fragment=new HomeFragment();
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.contentHacker,fragment,"");
                    fragmentTransaction.commit();
                    return true;

                case R.id.navhacker_profile:
                    actionBar.setTitle("Profile");
                    ProfileHackerFragment fragment1=new ProfileHackerFragment();
                    FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.contentHacker,fragment1,"");
                    fragmentTransaction1.commit();
                    return true;

                case R.id.navhacker_users:
                    actionBar.setTitle("Users");
                    UsersFragment fragment2=new UsersFragment();
                    FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.contentHacker,fragment2,"");
                    fragmentTransaction2.commit();
                    return true;
            }
            return false;
        }
    };
}