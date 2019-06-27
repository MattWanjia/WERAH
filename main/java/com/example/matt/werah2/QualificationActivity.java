package com.example.matt.werah2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import javax.xml.transform.Templates;

public class QualificationActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView professions, workType;
    private EditText experience;
    private Spinner profSpin, workTypeSpin;
    private Button toEducation;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    String userId, experienceNo;

    private DatabaseReference usersQualificationsInfoDB;

    ArrayAdapter<CharSequence> professionsAdapter;
    ArrayAdapter<CharSequence> worktypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        userId = user.getUid();

        usersQualificationsInfoDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Qualifications");

        professions = findViewById(R.id.professionsTV);
        workType = findViewById(R.id.worktypeTV);

        experience = findViewById(R.id.professionsXP);

        profSpin = findViewById(R.id.professionsSpinner);
        professionsAdapter = ArrayAdapter.createFromResource(this, R.array.professions, R.layout.support_simple_spinner_dropdown_item);
        profSpin.setAdapter(professionsAdapter);

        workTypeSpin = findViewById(R.id.spinnerWorkType);
        worktypeAdapter = ArrayAdapter.createFromResource(this, R.array.work_type, R.layout.support_simple_spinner_dropdown_item);
        workTypeSpin.setAdapter(worktypeAdapter);
        //workTypeSpin.setOnItemClickListener(this);

        toEducation = findViewById(R.id.toEducation);
        toEducation.setOnClickListener(this);

        //displayQualifications();
    }

    @Override
    public void onClick(View v) {
        if (v == toEducation){
            saveQualifications();
            startActivity(new Intent(QualificationActivity.this, EducationInfo.class));
        }
    }

    public void saveQualifications(){
        experienceNo = experience.getText().toString();

        if (experienceNo.isEmpty()){
            return;
        }

        String selectedProfession = profSpin.getSelectedItem().toString();
        String selectedWorkType = workTypeSpin.getSelectedItem().toString();

        Map userQualificationInfo = new HashMap();
        userQualificationInfo.put("PROFESSION", selectedProfession);
        userQualificationInfo.put("EXPERIENCE", experienceNo);
        userQualificationInfo.put("WORK-TYPE", selectedWorkType);

        usersQualificationsInfoDB.updateChildren(userQualificationInfo);
    }

    public void displayQualifications(){
        usersQualificationsInfoDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("PROFESSION") != null) {
                        String retrievedProfession = map.get("PROFESSION").toString();
                        profSpin.setSelection(Integer.parseInt(retrievedProfession));
                    }

                    if (map.get("EXPERIENCE") != null) {
                        String retrievedXP = map.get("EXPERIENCE").toString();
                        experience.setText(retrievedXP);
                    }

                    if (map.get("WORK-TYPE") != null) {
                        String retrievedWorkType = map.get("WORK-TYPE").toString();
                        workTypeSpin.setSelection(Integer.parseInt(retrievedWorkType));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
