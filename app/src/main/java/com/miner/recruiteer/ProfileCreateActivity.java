package com.miner.recruiteer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.miner.recruiteer.Class.LogIn;
import com.miner.recruiteer.Class.Profile;

import java.util.UUID;

public class ProfileCreateActivity extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 100;

    private EditText etProfileName,etProfilePhone,etProfileAddress;
    private Button btnProfileConfirm;
    private ImageButton imageBtnProfile;
    private CountryCodePicker ccp;
    String user = "";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    Uri imageURI;
    boolean imageAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);
        user = getIntent().getStringExtra("user");
        InitializeView();
    }

    private void InitializeView() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user);
        etProfileName = findViewById(R.id.etProfileName);
        etProfilePhone = findViewById(R.id.etProfilePhone);
        etProfileAddress = findViewById(R.id.etProfileAddress);

        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(etProfilePhone);

        btnProfileConfirm = findViewById(R.id.btnProfileConfirm);
        imageBtnProfile = findViewById(R.id.imageBtnProfile);
        imageBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQ_CODE);
            }
        });

        btnProfileConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckField();
            }
        });
    }

    private void CheckField() {
        if(imageAdded==true &&
                etProfileName.getText().toString().trim().isEmpty()==false &&
                etProfileAddress.getText().toString().trim().isEmpty()==false &&
                etProfilePhone.getText().toString().trim().isEmpty()==false){

            LogIn info = (LogIn) getIntent().getSerializableExtra("information");
            databaseReference.push().setValue(info);

            UUID id = UUID.randomUUID();
            Profile profile = new Profile(id.toString(),info.getEmail(),etProfileName.getText().toString(),etProfileAddress.getText().toString(),ccp.getFullNumberWithPlus().toString().trim());

            UploadData(profile);
        }else {
            if(etProfileName.getText().toString().isEmpty()) etProfileName.setError("*Required Field");
            if(etProfileAddress.getText().toString().isEmpty()) etProfileAddress.setError("*Required Field");
            if(etProfilePhone.getText().toString().isEmpty()) etProfilePhone.setError("*Required Field");
            if(imageAdded==false) Toast.makeText(this, "You need to add image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadData(Profile profile) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        String imageName = profile.getEmail().replace("@","-");
        storageReference = FirebaseStorage.getInstance().getReference(user+"/"+imageName.replace(".","-"));
        storageReference.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(progressDialog.isShowing()==true){
                            progressDialog.dismiss();
                            Toast.makeText(ProfileCreateActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing()==true) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileCreateActivity.this, "Upload failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        progressDialog.setTitle("Uploading data...");
        progressDialog.show();

        db.collection(user).document(profile.getId()).set(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfileCreateActivity.this, "Profile created successfully.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if(user.equals("employer")){
                            startActivity(new Intent(ProfileCreateActivity.this,EmployerHomeActivity.class)
                                    .putExtra("email",profile.getEmail()));
                        } else {
                            startActivity(new Intent(ProfileCreateActivity.this,HomeActivity.class)
                                    .putExtra("email",profile.getEmail()));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileCreateActivity.this, "Profile create failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
                imageURI = data.getData();
                imageBtnProfile.setImageURI(imageURI);
                imageAdded = true;
            }
        }
    }

}