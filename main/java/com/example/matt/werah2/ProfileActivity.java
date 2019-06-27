package com.example.matt.werah2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    ImageView profileImage;
    EditText names, preferredNames, dateOfBirth;
    RadioGroup radioGroup;
    Button proceedToQualifications;

    DatePicker datePicker;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference usersPersonalInfoDB;
    private StorageReference imageFilePath;

    String userId, userName, userNickname, profileImageUrl, dateOB;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        usersPersonalInfoDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Personal Info");

        dateOfBirth = findViewById(R.id.profileDOB);
        dateOfBirth.setOnClickListener(this);

        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(this);

        names = findViewById(R.id.profileName);
        preferredNames = findViewById(R.id.profileNickname);

        proceedToQualifications = findViewById(R.id.proceedToQualifications);
        proceedToQualifications.setOnClickListener(this);

        displayUserInfo();
    }

    @Override
    public void onClick(View v) {
        if (v == proceedToQualifications){
            savePersonalInfo();
            startActivity(new Intent(ProfileActivity.this, QualificationActivity.class));
        }
        if (v == profileImage){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }
        if (v == dateOfBirth){
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getFragmentManager(), "date");
        }
    }

    public void savePersonalInfo(){
        radioGroup = findViewById(R.id.profileRG);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedId);

        userName = names.getText().toString();
        userNickname = preferredNames.getText().toString();
        dateOB = dateOfBirth.getText().toString();

        if (userName.isEmpty() || userNickname.isEmpty() || radioButton.getText() == null || dateOB.isEmpty()){
            return;
        }

        //DatabaseReference startUpInfoName = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Information").child("Names");
        //startUpInfoName.setValue(userName);

        //DatabaseReference startUpInfoNickName = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Information").child("Preferred name");
        //startUpInfoNickName.setValue(userNickname);

        //DatabaseReference startUpInfoSex = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Information").child("Sex");
        //startUpInfoSex.setValue(radioButton.getText());
        Map userPersonalInfo = new HashMap();
        userPersonalInfo.put("UserNames", userName);
        userPersonalInfo.put("UserNickname", userNickname);
        userPersonalInfo.put("UserSex", radioButton.getText());
        userPersonalInfo.put("DOB", dateOB);
        usersPersonalInfoDB.updateChildren(userPersonalInfo);

        if (resultUri != null){
            imageFilePath = FirebaseStorage.getInstance().getReference().child("Users").child(userId).child("Images").child("Profile Images");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageFilePath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map userPersonalInfo = new HashMap();
                    userPersonalInfo.put("profileImageUrl", downloadUrl);
                    usersPersonalInfoDB.updateChildren(userPersonalInfo);

                    finish();
                    return;
                }
            });

        }else {
            finish();
        }
    }

    public void displayUserInfo(){
        usersPersonalInfoDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("UserNames") != null){
                        String retreivedNames = map.get("UserNames").toString();
                        names.setText(retreivedNames);
                    }
                    if (map.get("UserNickname") != null){
                        String retreivedNicknames = map.get("UserNickname").toString();
                        preferredNames.setText(retreivedNicknames);
                    }
                    //radiobutton retrieving
                    if (map.get("UserSex") != null){
                        String retrievedSex = map.get("UserSex").toString();
                        //radioGroup.setSelected(Boolean.parseBoolean(retrievedSex));
                        if (retrievedSex == "Male"){
                            //radioGroup.setId(R.id.radioBtnMale);
                            (radioGroup.getChildAt(0)).setSelected(true);
                        }else if (retrievedSex == "Female") {
                            //radioGroup.setId(R.id.radioBtnFemale);
                            //(radioGroup.getChildAt(1)).setActivated(true);
                            (radioGroup.getChildAt(1)).setSelected(true);
                        }
                    }
                    if (map.get("profileImageUrl") != null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        //Glide.with(getApplicationContext()).load(profileImageUrl).into(profileImage);
                        Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(imageFilePath).into(profileImage);
                    }

                    if (map.get("DOB") != null){
                        String retreivedDOB = map.get("DOB").toString();
                        dateOfBirth.setText(retreivedDOB);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 ){
            final Uri imageUri = data.getData();
            resultUri = imageUri;

            profileImage.setImageURI(resultUri);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        setDate(calendar);
    }

    private void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateOfBirth.setText(dateFormat.format(calendar.getTime()));
    }

    public static class DatePickerFragment extends DialogFragment {
        int year;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)getActivity(),
                    year, month, day);
        }

        public int getYear(TextView yearTV){
            yearTV.setText(year);
            return year;
        }
    }
}
