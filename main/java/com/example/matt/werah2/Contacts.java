package com.example.matt.werah2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Contacts extends AppCompatActivity implements View.OnClickListener{
    private TextView contactsTV;
    private EditText phone1, phone2, alternateEmail;
    private Button done;

    private String phone1IP, phone2IP, alternateEmailIP, userId;
    private static String CONTACTS = "CONTACTS";
    private static String Users = "Users";
    private static String PHONE1 = "PHONE1";
    private static String PHONE2 = "PHONE2";
    private static String SUCCESS = "SUCCESS";
    private static String TRY_AGAIN = "TRY AGAIN";
    private static String ALTERNATE_EMAIL = "ALTERNATE EMAIL";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference userContactDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        userContactDB = FirebaseDatabase.getInstance().getReference().child(Users).child(userId).child(CONTACTS);

        contactsTV = findViewById(R.id.contactsTV);

        phone1 = findViewById(R.id.phone1ET);
        phone2 = findViewById(R.id.phone2ET);
        alternateEmail = findViewById(R.id.alternateEmail);

        done = findViewById(R.id.doneBtn);
        done.setOnClickListener(this);

        displayContacts();
    }

    @Override
    public void onClick(View v) {
        if (v == done){
            saveContacts();
        }
    }

    public void saveContacts(){
        phone1IP = phone1.getText().toString();
        phone2IP = phone2.getText().toString();

        alternateEmailIP = alternateEmail.getText().toString();

        if (phone1IP.isEmpty()){
            Toast.makeText(getApplication(), "PLEASE ENTER PHONE NUMBER", Toast.LENGTH_SHORT).show();
            return;
        }

        Map userContacts = new HashMap();
        userContacts.put(PHONE1, phone1IP);
        userContacts.put(PHONE2, phone2IP);
        userContacts.put(ALTERNATE_EMAIL, alternateEmailIP);

        userContactDB.updateChildren(userContacts).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Contacts.this, CardActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), TRY_AGAIN, Toast.LENGTH_SHORT);
            }
        });
    }

    public void displayContacts(){
        userContactDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get(PHONE1) != null){
                        String retrievedPhone1 = map.get(PHONE1).toString();
                        phone1.setText(retrievedPhone1);
                    }
                    if (map.get(PHONE2) != null){
                        String retrievedPhone2 = map.get(PHONE2).toString();
                        phone2.setText(retrievedPhone2);
                    }
                    if (map.get(ALTERNATE_EMAIL) != null){
                        String retrievedAlternateEmail = map.get(ALTERNATE_EMAIL).toString();
                        phone1.setText(retrievedAlternateEmail);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
