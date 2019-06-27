package com.example.matt.werah2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PostAJobActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView pjHeader, spinner1TV, spinner2TV;
    private EditText jobTitle, jobLocation, jobExperience, jobSalary, numberWanted;
    private Spinner worktypePost, fieldPost;
    private Button postJob;

    ArrayAdapter<CharSequence> fieldsAdapter;
    ArrayAdapter<CharSequence> worktypesAdapter;

    private DatabaseReference jobsPostedByUserDB;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userId, postedTitle, workField, postedLocation, postedExperience, workType, postedSalary, manPower;
    final String Users = "Users";
    final String JOBS = "JOBS";
    final String JOBS_I_POSTED = "JOBS_I_POSTED";
    final String postedTitleMap = "JOB_TITLE";
    final String workFieldMap = "JOB_FIELD";
    final String postedLocationMap = "JOB_LOCATION";
    final String postedExperienceMap = "JOB_EXPERIENCE";
    final String workTypeMap = "JOB_TYPE";
    final String postedSalaryMap = "JOB_SALARY";
    final String manPowerMap = "WORKFORCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ajob);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {}
        };
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        jobsPostedByUserDB = FirebaseDatabase.getInstance().getReference().child(Users).child(userId).child(JOBS).child(JOBS_I_POSTED).push();

        pjHeader = findViewById(R.id.postJobTV);
        spinner1TV = findViewById(R.id.postFieldTV);
        spinner2TV = findViewById(R.id.postJobworktypeTV);

        jobTitle = findViewById(R.id.postJobTitle);
        jobLocation = findViewById(R.id.postJobLocation);
        jobExperience = findViewById(R.id.postJobExperience);
        jobSalary = findViewById(R.id.postJobSalary);
        numberWanted = findViewById(R.id.postJobPeopleWanted);

        fieldPost= findViewById(R.id.postJobFieldSpinner);
        fieldsAdapter = ArrayAdapter.createFromResource(this, R.array.professions, R.layout.support_simple_spinner_dropdown_item);
        fieldPost.setAdapter(fieldsAdapter);

        worktypePost = findViewById(R.id.postJobspinnerWorkType);
        worktypesAdapter = ArrayAdapter.createFromResource(this, R.array.work_type, R.layout.support_simple_spinner_dropdown_item);
        worktypePost.setAdapter(worktypesAdapter);

        postJob = findViewById(R.id.postJobBtn);
        postJob.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == postJob){
            savePostedJob();
        }
    }

    public void savePostedJob(){
        postedTitle = jobTitle.getText().toString();
        workField = fieldPost.getSelectedItem().toString();
        postedLocation = jobLocation.getText().toString();
        postedExperience = jobExperience.getText().toString();
        workType = worktypePost.getSelectedItem().toString();
        postedSalary = jobSalary.getText().toString();
        manPower = numberWanted.getText().toString();

        Map jobPostedByUser = new HashMap();
        jobPostedByUser.put(postedTitleMap, postedTitle);
        jobPostedByUser.put(workFieldMap, workField);
        jobPostedByUser.put(postedLocationMap, postedLocation);
        jobPostedByUser.put(postedExperienceMap, postedExperience);
        jobPostedByUser.put(workTypeMap, workType);
        jobPostedByUser.put(postedSalaryMap, postedSalary);
        jobPostedByUser.put(manPowerMap, manPower);

        jobsPostedByUserDB.updateChildren(jobPostedByUser).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getApplication(), "JOB POSTED", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), JobsPosted.class));
            }
        });
    }
}
