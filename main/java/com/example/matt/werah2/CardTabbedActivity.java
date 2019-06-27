package com.example.matt.werah2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CardTabbedActivity extends AppCompatActivity {
    private CustomPagerAdapter customAdapter;
    private CustomViewPager customViewPager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_tabbed);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        customAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        customViewPager = findViewById(R.id.container);
        customViewPager.setAdapter(customAdapter);
        customViewPager.setSwipeable(false);

        TabLayout tabLayout = findViewById(R.id.tabs);

        customViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(customViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.profile){
            Toast.makeText(getApplicationContext(), "PROFILE", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CardTabbedActivity.this, ProfileActivity.class));
        }
        if (item.getItemId() == R.id.signout){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(getApplicationContext(), "PLEASE SIGN IN", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

}
