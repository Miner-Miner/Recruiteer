package com.miner.recruiteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etEmail,etPass;
    Button btnRegister,btnLogin;
    LottieAnimationView lottieSwitch;
    final String[] switchString = {"employer"};

    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //startActivity(new Intent(this,RegisterOTPActivity.class));
        InitializeView();
        OnButtonClick();
    }

    private void OnButtonClick() {
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void InitializeView(){
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        lottieSwitch = findViewById(R.id.lottieSwitch);
        final Boolean[] switchBool = {true};

        lottieSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchBool[0] ==true){
                    switchString[0] = "employee";
                    lottieSwitch.setMinAndMaxFrame(0,90);
                    lottieSwitch.setSpeed(3.5f);
                    lottieSwitch.playAnimation();
                    switchBool[0] =false;
                } else {
                    switchString[0] = "employer";
                    lottieSwitch.setMinAndMaxFrame(90,200);
                    lottieSwitch.setSpeed(3.5f);
                    lottieSwitch.playAnimation();
                    switchBool[0] =true;
                }
                Toast.makeText(LoginActivity.this, switchString[0], Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag().toString().equals("register")){
            startActivity(new Intent(this,RegisterActivity.class).putExtra("user",switchString[0]));
        }
        else{
            //Check Email Format
            String email = etEmail.getText().toString().trim();
            String password = etPass.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()){
                if(email.isEmpty()){
                    etEmail.setError("*Required Field");
                }
                if(password.isEmpty()){
                    etPass.setError("*Required Field");
                }
            }
            else{
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches()==true){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(switchString[0]);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()==false){
                                Toast.makeText(LoginActivity.this, "There is no data in database", Toast.LENGTH_SHORT).show();
                            } else {
                                EmailPass((Map<String,Object>) snapshot.getValue(),email,password);;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    etEmail.setError("**Email Format Wrong");
                }
            }
        }
    }

    private void EmailPass(Map<String, Object> users, String email, String password) {

        Boolean login = false;
        ArrayList<String> emailArray = new ArrayList<>();
        ArrayList<String> passwordArray = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            emailArray.add(String.valueOf(singleUser.get("email")));
            passwordArray.add(String.valueOf(singleUser.get("password")));
        }
        for(int i=0 ;i<emailArray.size() ;i++){
            if (emailArray.get(i).equals(email) && passwordArray.get(i).equals(password)){
                if(switchString[0].equals("employer")){
                    login = true;
                    startActivity(new Intent(this,EmployerHomeActivity.class)
                            .putExtra("email",email));
                    finish();
                } else {
                    login = true;
                    startActivity(new Intent(this,HomeActivity.class)
                            .putExtra("email",email));
                    finish();
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setMessage("Email Or Password is Wrong!!!\nTry again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        if(login==false){
            alertDialog.show();
        }
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