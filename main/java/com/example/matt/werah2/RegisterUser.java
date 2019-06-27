package com.example.matt.werah2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView;
    private EditText emailET, passwordET, nameET;
    private Button register;

    private RadioGroup radioGroup;

    private String inputEmail, inputPassword, userNames;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //MainActivity starts
                if (user != null){
                    startActivity(new Intent(getApplicationContext(), CardActivity.class));
                }
            }
        };

        imageView = findViewById(R.id.imageRegistration);

        //nameET = findViewById(R.id.registrationNames);

        emailET = findViewById(R.id.registrationEmail);
        passwordET = findViewById(R.id.registrationPassword);

        register = findViewById(R.id.registerBtn);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        registerUser();
    }

    private void registerUser() {
        inputEmail = emailET.getText().toString();
        inputPassword = passwordET.getText().toString();

        if (inputEmail.isEmpty() || inputPassword.isEmpty()){
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "WELCOME", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CardActivity.class));
                    //storeUserStartUpInfo();
                }else{
                    Toast.makeText(getApplicationContext(), "TRY AGAIN", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    /*public void storeUserStartUpInfo(){
        userNames = nameET.getText().toString();
        if (userNames.isEmpty()){
            return;
        }

        radioGroup =findViewById(R.id.regSexRG);

        int selectedId = radioGroup.getCheckedRadioButtonId();

        final RadioButton radioButton = findViewById(selectedId);

        if(radioButton.getText() == null){
            return;
        }

        String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference startUpInfoName = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Information").child("Names");
        startUpInfoName.setValue(userNames);

        DatabaseReference startUpInfoSex = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Information").child("Sex");
        startUpInfoSex.setValue(radioButton.getText());
    }*/

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
