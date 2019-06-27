package com.example.matt.werah2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EducationInfo extends AppCompatActivity implements View.OnClickListener{
    private TextView education;
    private EditText phdTV, mastersTV, undergradTV, collegeTV, highschoolTV;
    private Button toDocuments;

    private FirebaseAuth firebaseAuth;
    private String userId, phdIP, mastersIP, underGradIP, collegeIP, highschoolIP;

    private DatabaseReference userEducationInfoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_info);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        userEducationInfoDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Education");

        education = findViewById(R.id.educationTV);

        phdTV = findViewById(R.id.universitypHDtV);
        mastersTV = findViewById(R.id.universityMastersTV);
        undergradTV = findViewById(R.id.universityUndergradTV);
        collegeTV = findViewById(R.id.collegeTV);
        highschoolTV = findViewById(R.id.highschoolTV);

        toDocuments = findViewById(R.id.toDocuments);
        toDocuments.setOnClickListener(this);

        displayEduInfo();
    }

    @Override
    public void onClick(View v) {
        if (v == toDocuments){
            saveEduInfo();
            startActivity(new Intent(EducationInfo.this, Documents.class));
        }
    }

    public void saveEduInfo(){
        phdIP = phdTV.getText().toString();
        mastersIP = mastersTV.getText().toString();
        underGradIP = undergradTV.getText().toString();
        collegeIP = collegeTV.getText().toString();
        highschoolIP = highschoolTV.getText().toString();

        Map userEducationInfo = new HashMap();
        userEducationInfo.put("PhD", phdIP);
        userEducationInfo.put("Masters", mastersIP);
        userEducationInfo.put("Undergraduate", underGradIP);
        userEducationInfo.put("College", collegeIP);
        userEducationInfo.put("HighSchool", highschoolIP);

        userEducationInfoDB.updateChildren(userEducationInfo);
    }

    public void displayEduInfo(){
        userEducationInfoDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("PhD") != null){
                        String retreivedPhD = map.get("PhD").toString();
                        phdTV.setText(retreivedPhD);
                    }
                    if (map.get("Masters") != null){
                        String retreivedMasters = map.get("Masters").toString();
                        mastersTV.setText(retreivedMasters);
                    }
                    if (map.get("Undergraduate") != null){
                        String retreivedUndergrad = map.get("Undergraduate").toString();
                        undergradTV.setText(retreivedUndergrad);
                    }
                    if (map.get("College") != null){
                        String retreivedCollege = map.get("College").toString();
                        collegeTV.setText(retreivedCollege);
                    }
                    if (map.get("HighSchool") != null){
                        String retreivedHighSchool = map.get("HighSchool").toString();
                        highschoolTV.setText(retreivedHighSchool);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
