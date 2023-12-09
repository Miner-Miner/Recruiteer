package com.miner.recruiteer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AdminLoginActivity extends AppCompatActivity {

    EditText etAdminUser,etAdminPass;
    Button btnAdminLogin;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        etAdminUser = findViewById(R.id.etAdminUser);
        etAdminPass = findViewById(R.id.etAdminPass);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        map.get("user").toString();
                        map.get("password").toString();
                        //Toast.makeText(AdminLoginActivity.this, map.toString(), Toast.LENGTH_SHORT).show();
                        if(etAdminUser.getText().toString().equals(map.get("user").toString()) && etAdminPass.getText().toString().equals(map.get("password"))){
                            Toast.makeText(AdminLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminLoginActivity.this,AdminHomeActivity.class));
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminLoginActivity.this);
                            builder.setCancelable(false)
                                    .setMessage("Username or Password Wrong")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you wanna exit Recruiteer")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
         alertDialog.show();
    }
}