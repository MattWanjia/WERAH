package com.example.matt.werah2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class JobsPosted extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;
    private Cards card_data[];
    private CustomArrayAdapter customArrayAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    final String Users = "Users";
    final String JOBS = "JOBS";
    final String JOBS_I_POSTED = "JOBS_I_POSTED";
    private String userId;

    DatabaseReference jobsPostedByUserDB;
    String jobsPostedByUserDBpushKey;
    ListView listView;
    List<Cards> rowItems;

    FloatingActionButton floatingActionButton;
    private String POST_A_JOB = "POST A JOB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_posted);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        floatingActionButton = findViewById(R.id.fabPostJob);
        floatingActionButton.setOnClickListener(this);
        floatingActionButton.setOnLongClickListener(this);

        /*al = new ArrayList<>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");*/

        getUserJobs();

        rowItems = new ArrayList<Cards>();

        //arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.jobType, al );

        customArrayAdapter = new CustomArrayAdapter(this, R.layout.item_jobs_i_posted, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        //flingContainer.setAdapter(arrayAdapter);
        flingContainer.setAdapter(customArrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                //al.remove(0);
                rowItems.remove(0);
                //arrayAdapter.notifyDataSetChanged();
                customArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(JobsPosted.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(JobsPosted.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //al.add("XML ".concat(String.valueOf(i)));
                //rowItems.add("XML ".concat(String.valueOf(i)));
                //arrayAdapter.notifyDataSetChanged();
                //customArrayAdapter.notifyDataSetChanged();
                //Log.d("LIST", "notified");
                //i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(JobsPosted.this, "Click!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jobspostedmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeJobsPosted:
                startActivity(new Intent(getApplicationContext(), CardActivity.class));

            case R.id.signout2:
                firebaseAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getUserJobs(){
        jobsPostedByUserDB = FirebaseDatabase.getInstance().getReference().child(Users).child(userId).child(JOBS).child(JOBS_I_POSTED).child(jobsPostedByUserDBpushKey);
        jobsPostedByUserDBpushKey = FirebaseDatabase.getInstance().getReference().child(Users).child(userId).child(JOBS).child(JOBS_I_POSTED).push().getKey();

        jobsPostedByUserDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    Cards item = new Cards(dataSnapshot.child("JOB_FIELD").getValue().toString(),dataSnapshot.child("JOB_TITLE").getValue().toString());
                    rowItems.add(item);
                    customArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        firebaseAuth.addAuthStateListener(authStateListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        firebaseAuth.removeAuthStateListener(authStateListener);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButton){
            startActivity(new Intent(getApplicationContext(), PostAJobActivity.class));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(getApplicationContext(), POST_A_JOB, Toast.LENGTH_SHORT).show();
        return true;
    }
}
