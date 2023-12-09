package com.miner.recruiteer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EmployerHomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddJob,btnCheckDeleteJob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);
        InitiateView();
    }

    private void InitiateView() {
        btnAddJob = (Button) findViewById(R.id.btnAddJob);
        btnCheckDeleteJob = (Button) findViewById(R.id.btnCheckDeleteJob);

        btnAddJob.setOnClickListener(this);
        btnCheckDeleteJob.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if ("add".equals(tag)) {
            startActivity(new Intent(this,AddJobActivity.class).putExtra("email",getIntent().getStringExtra("email")));
        } else {
            startActivity(new Intent(this, UpdateDeleteJobActivity.class).putExtra("email",getIntent().getStringExtra("email")));
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