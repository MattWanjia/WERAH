package com.example.matt.werah2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    Button goToSignIn, goToRegister;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null){
                    startActivity(new Intent(MainActivity.this, CardActivity.class));
                }
            }
        };

        imageView = findViewById(R.id.mainActImage);

        goToSignIn = findViewById(R.id.signin);
        goToSignIn.setOnClickListener(this);

        goToRegister = findViewById(R.id.signup);
        goToRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == goToSignIn){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            Toast.makeText(getApplicationContext(), "LOGIN", Toast.LENGTH_SHORT).show();

        }
        if (view == goToRegister){
            startActivity(new Intent(getApplicationContext(), RegisterUser.class));
            Toast.makeText(getApplicationContext(), "REGISTER", Toast.LENGTH_SHORT).show();
        }
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
