package com.miner.recruiteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miner.recruiteer.Class.LogIn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity{

    EditText etEmailReg, etPassReg, etPassRegConf;
    Button btnConfirm;
    private DatabaseReference databaseReference;
    private static String errorMsg = "Password length must have at least 8 character !!\n" +
            "Password must have at least one special character !!\n"+
            "Password must have at least one uppercase character !!\n"+
            "Password must have at least one lowercase character !!\n"+
            "Password must have at least one digit character !!";
    private LogIn info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitializeView();
    }

    private void InitializeView() {
        etEmailReg = findViewById(R.id.etEmailReg);
        etPassReg = findViewById(R.id.etPassReg);
        etPassRegConf = findViewById(R.id.etPassRegConf);
        btnConfirm = findViewById(R.id.btnConfirm);
        final String user = getIntent().getStringExtra("user");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckEmailAndPassword(etEmailReg.getText().toString().trim(),etPassReg.getText().toString().trim(),etPassRegConf.getText().toString().trim())==true){
                    info = new LogIn(etEmailReg.getText().toString().trim(),etPassReg.getText().toString().trim());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean emailCheck = false;
                            if(snapshot.exists() && snapshot.getChildrenCount()>1){
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    if (dataSnapshot.child("email").getValue().toString().trim().equals(info.getEmail())){
                                        Toast.makeText(RegisterActivity.this, "Account Already exists", Toast.LENGTH_SHORT).show();
                                        emailCheck = true;
                                    }
                                }
                                if(emailCheck == false){
                                    startActivity(new Intent(RegisterActivity.this,ProfileCreateActivity.class)
                                            .putExtra("user",user)
                                            .putExtra("information",info));
                                }
                            }
                            else {
                                startActivity(new Intent(RegisterActivity.this,ProfileCreateActivity.class)
                                        .putExtra("user",user)
                                        .putExtra("information",info));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private boolean CheckEmailAndPassword(String mail, String pass, String confPass) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);
        if (matcher.matches() == false || pass.equals(confPass) == false || isValid(pass)==false) {
            if (matcher.matches() == false) etEmailReg.setError("*Email Format Wrong");
            if (pass.equals(confPass) == false) {
                etPassReg.setError("Password and Confirm password are not the same");
                etPassRegConf.setError("Password and Confirm password are not the same");
            }
            if(isValid(pass)==false){
                etPassReg.setError(errorMsg);
            }
            return false;
        } else
            return true;
    }

    public static boolean isValid(String password) {
        Pattern specialCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        boolean flag=true;
        if (password.length() < 8 || !specialCharPatten.matcher(password).find() || !UpperCasePatten.matcher(password).find() || !lowerCasePatten.matcher(password).find() || !digitCasePatten.matcher(password).find()) {
            flag=false;
        }
        return flag;
    }
}