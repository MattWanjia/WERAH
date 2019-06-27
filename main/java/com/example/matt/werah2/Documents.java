package com.example.matt.werah2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Documents extends AppCompatActivity implements View.OnClickListener{
    private TextView headerTV, cvTV, reLetterTV, idTV, passportTV;
    private EditText fileUrl1, fileUrl2, fileUrl3, fileUrl4;
    private Button upload1, upload2, upload3, upload4, toContacts;

    private static int CHOOSE_1_CODE = 1;
    private static int CHOOSE_2_CODE = 2;
    private static int CHOOSE_3_CODE = 3;
    private static int CHOOSE_4_CODE = 4;

    private Uri resultUri1, resultUri2, resultUri3, resultUri4;

    private StorageReference documentsStoreCV, documentsStoreLetter, documentsStoreID, documentsStorePassport;

    private static String Users = "Users";
    private static String SUCCESS = "SUCCESS";
    private static String TRY_AGAIN = "TRY AGAIN";
    private static String DOCUMENTS = "DOCUMENTS";
    private static String CV = "CV";
    private static String RECOMMENDATION_LETTER = "RECOMMENDATION_LETTER";
    private static String ID = "ID";
    private static String PASSPORT = "PASSPORT";
    private String userId;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        documentsStoreCV = FirebaseStorage.getInstance().getReference().child(Users).child(userId).child(DOCUMENTS).child(CV);
        documentsStoreLetter = FirebaseStorage.getInstance().getReference().child(Users).child(userId).child(DOCUMENTS).child(RECOMMENDATION_LETTER);
        documentsStoreID = FirebaseStorage.getInstance().getReference().child(Users).child(userId).child(DOCUMENTS).child(ID);
        documentsStorePassport = FirebaseStorage.getInstance().getReference().child(Users).child(userId).child(DOCUMENTS).child(PASSPORT);

        //static textviews
        headerTV = findViewById(R.id.documentsHeader);
        cvTV = findViewById(R.id.cvTV);
        reLetterTV = findViewById(R.id.recLetterTV);
        idTV = findViewById(R.id.idTV);
        passportTV = findViewById(R.id.passportTV);

        //dynamic textviews
        fileUrl1 = findViewById(R.id.fileUrl1);
        fileUrl1.setOnClickListener(this);

        fileUrl2 = findViewById(R.id.fileUrl2);
        fileUrl2.setOnClickListener(this);

        fileUrl3 = findViewById(R.id.fileUrl3);
        fileUrl3.setOnClickListener(this);

        fileUrl4 = findViewById(R.id.fileUrl4);
        fileUrl4.setOnClickListener(this);

        //buttons
        upload1 = findViewById(R.id.buttonChoose1);
        upload1.setOnClickListener(this);

        upload2 = findViewById(R.id.buttonChoose2);
        upload2.setOnClickListener(this);

        upload3 = findViewById(R.id.buttonChoose3);
        upload3.setOnClickListener(this);

        upload4 = findViewById(R.id.buttonChoose4);
        upload4.setOnClickListener(this);

        toContacts = findViewById(R.id.toContacts);
        toContacts.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == fileUrl1){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, CHOOSE_1_CODE);
        }
        else if (v == fileUrl2){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, CHOOSE_2_CODE);
        }
        else if (v == fileUrl3){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, CHOOSE_3_CODE);
        }
        else if (v == fileUrl4){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, CHOOSE_4_CODE);
        }
        else if (v == upload1){
            if (!fileUrl1.getText().toString().isEmpty()){
                saveCV();
            }else {
                return;
            }
        }
        else if (v == upload2){
            if (!fileUrl2.getText().toString().isEmpty()){
                saveRecommendationLetter();
            }else {
                return;
            }
        }
        else if (v == upload3){
            if (!fileUrl3.getText().toString().isEmpty()){
                saveID();
            }else {
                return;
            }
        }
        else if (v == upload4){
            if (!fileUrl4.getText().toString().isEmpty()){
                savePassport();
            }else {
                return;
            }
        }
        else if (v == toContacts){
            startActivity(new Intent(Documents.this, ContactSplash.class));
        }
    }

    public void saveCV(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("UPLOADING...");
        progressDialog.show();

        if (resultUri1.toString() != null && fileUrl1.getText().toString()!=null){
            documentsStoreCV.putFile(resultUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    return;
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int transferredData = (int) taskSnapshot.getBytesTransferred();
                    int totalData = (int) taskSnapshot.getTotalByteCount();

                    int percentStored = (transferredData/totalData)* 100;
                    progressDialog.setMessage(percentStored + "% complete");
                }
            });
        }
    }

    public void saveRecommendationLetter(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("UPLOADING...");
        progressDialog.show();

        if (resultUri2.toString() != null){
            documentsStoreLetter.putFile(resultUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    return;
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int transferredData = (int) taskSnapshot.getBytesTransferred();
                    int totalData = (int) taskSnapshot.getTotalByteCount();

                    int percentStored = (transferredData/totalData)* 100;
                    progressDialog.setMessage(percentStored + "% complete");
                }
            });
        }
    }

    public void saveID(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("UPLOADING...");
        progressDialog.show();

        if (resultUri3.toString() != null){
            documentsStoreID.putFile(resultUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    return;
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int transferredData = (int) taskSnapshot.getBytesTransferred();
                    int totalData = (int) taskSnapshot.getTotalByteCount();

                    int percentStored = (transferredData/totalData)* 100;
                    progressDialog.setMessage(percentStored + "% complete");
                }
            });
        }
    }

    public void savePassport(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("UPLOADING...");
        progressDialog.show();

        if (resultUri4.toString() != null){
            documentsStorePassport.putFile(resultUri4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    return;
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int transferredData = (int) taskSnapshot.getBytesTransferred();
                    int totalData = (int) taskSnapshot.getTotalByteCount();

                    int percentStored = (transferredData/totalData)* 100;
                    progressDialog.setMessage(percentStored + "% complete");
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_1_CODE){
            resultUri1 = data.getData();
            if (resultUri1 != null){
                fileUrl1.setText(resultUri1.toString());
            }
        }else if (requestCode == CHOOSE_2_CODE){
            resultUri2 = data.getData();
            if (resultUri2 != null){
                fileUrl2.setText(resultUri2.toString());
            }
        }else if (requestCode == CHOOSE_3_CODE){
            resultUri3 = data.getData();
            if (resultUri3 != null){
                fileUrl3.setText(resultUri3.toString());
            }
        }else if (requestCode == CHOOSE_4_CODE){
            resultUri4 = data.getData();
            if (resultUri4 != null){
                fileUrl4.setText(resultUri4.toString());
            }
        }
    }
}
