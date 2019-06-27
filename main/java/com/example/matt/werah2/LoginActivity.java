package com.example.matt.werah2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private EditText emailET, passwordET;
    private Button login;

    private String inputEmail, inputPassword;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null){
                    startActivity(new Intent(getApplicationContext(), CardActivity.class));
                }
            }
        };

        imageView = findViewById(R.id.imageLogin);

        emailET = findViewById(R.id.loginEmail);
        passwordET = findViewById(R.id.loginPassword);

        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        loginUser();
    }

    private void loginUser() {
        inputEmail = emailET.getText().toString();
        inputPassword = passwordET.getText().toString();

        if (inputEmail.isEmpty() || inputPassword.isEmpty()){
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "WELCOME", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CardActivity.class));
                }else {
                    Toast.makeText(getApplicationContext(), "TRY AGAIN", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
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
