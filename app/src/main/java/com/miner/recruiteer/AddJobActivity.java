package com.miner.recruiteer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miner.recruiteer.Class.JobPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddJobActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etJobTitle,etJobSalary,etRequirement,etRequirement2,etRequirement3,etRequirement4,etRequirement5,etSummary;
    Button btnAddRequirement,btnAddRequirement2,btnAddRequirement3,btnAddRequirement4,btnAddRequirement5;
    Button btnRemoveRequirement2,btnRemoveRequirement3,btnRemoveRequirement4,btnRemoveRequirement5,btnAddPost;
    LinearLayout row2,row3,row4,row5;
    Spinner spinnerPost,spinnerTag1,spinnerTag2,spinnerTag3;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> reservePost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        InitiateView();
    }

    private void InitiateView() {
        etJobTitle = (EditText) findViewById(R.id.etJobTitle);
        etJobSalary = (EditText) findViewById(R.id.etJobSalary);
        etRequirement = (EditText) findViewById(R.id.etRequirement);
        etRequirement2 = (EditText) findViewById(R.id.etRequirement2);
        etRequirement3 = (EditText) findViewById(R.id.etRequirement3);
        etRequirement4 = (EditText) findViewById(R.id.etRequirement4);
        etRequirement5 = (EditText) findViewById(R.id.etRequirement5);
        etSummary = (EditText) findViewById(R.id.etJobSummary);

        btnAddRequirement = findViewById(R.id.btnAddRequirement);
        btnAddRequirement2 = findViewById(R.id.btnAddRequirement2);
        btnAddRequirement3 = findViewById(R.id.btnAddRequirement3);
        btnAddRequirement4 = findViewById(R.id.btnAddRequirement4);
        btnAddRequirement5 = findViewById(R.id.btnAddRequirement5);
        btnAddRequirement.setOnClickListener(this);
        btnAddRequirement2.setOnClickListener(this);
        btnAddRequirement3.setOnClickListener(this);
        btnAddRequirement4.setOnClickListener(this);
        btnAddRequirement5.setOnClickListener(this);

        btnRemoveRequirement2 = findViewById(R.id.btnRemoveRequirement2);
        btnRemoveRequirement3 = findViewById(R.id.btnRemoveRequirement3);
        btnRemoveRequirement4 = findViewById(R.id.btnRemoveRequirement4);
        btnRemoveRequirement5 = findViewById(R.id.btnRemoveRequirement5);
        btnRemoveRequirement2.setOnClickListener(this);
        btnRemoveRequirement3.setOnClickListener(this);
        btnRemoveRequirement4.setOnClickListener(this);
        btnRemoveRequirement5.setOnClickListener(this);

        row2 = findViewById(R.id.row2);
        row3 = findViewById(R.id.row3);
        row4 = findViewById(R.id.row4);
        row5 = findViewById(R.id.row5);

        spinnerPost = findViewById(R.id.spinnerPost);
        spinnerTag1 = findViewById(R.id.spinnerTag1);
        spinnerTag2 = findViewById(R.id.spinnerTag2);
        spinnerTag3 = findViewById(R.id.spinnerTag3);

        reservePost.add("");
        reservePost.add("1 post");
        reservePost.add("2 posts");
        reservePost.add("3 posts");
        reservePost.add("4 posts");
        reservePost.add("5 posts");
        reservePost.add("6 posts");
        reservePost.add("7 posts");
        reservePost.add("8 posts");
        reservePost.add("9 posts");
        reservePost.add("10 posts");

        ArrayAdapter spinnerPostAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, reservePost);
        spinnerPostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPost.setAdapter(spinnerPostAdapter);

        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(this,R.array.tag, android.R.layout.simple_spinner_item);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTag1.setAdapter(tagAdapter);
        spinnerTag2.setAdapter(tagAdapter);
        spinnerTag3.setAdapter(tagAdapter);

        btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckField();
            }
        });

    }

    private void CheckField() {
        final Boolean[] checkPass = {true};
        String errorMsg="";
        String email = getIntent().getStringExtra("email");
        String title = etJobTitle.getText().toString().trim();
        String salary = etJobSalary.getText().toString().trim();
        String summary = etSummary.getText().toString().trim();

        if(spinnerPost.getSelectedItem().toString().trim().equals(reservePost.get(0))){
            errorMsg += "You need to select posts.\n";
            checkPass[0] = false;
        }
        if(title.isEmpty()) {
            etJobTitle.setError("You need to add Job Title.");
            checkPass[0] = false;
        }
        if(salary.isEmpty()){
            etJobSalary.setError("You need to add Salary");
            checkPass[0] = false;
        }
        if(summary.isEmpty()){
            etSummary.setError("You need to add Summary");
            checkPass[0] = false;
        }
        if(etRequirement.getText().toString().trim().isEmpty()){
            etRequirement.setError("You need to add requirement");
            checkPass[0] = false;
        }
        if(spinnerTag1.getSelectedItem().toString().trim().isEmpty() && spinnerTag2.getSelectedItem().toString().trim().isEmpty() && spinnerTag3.getSelectedItem().toString().trim().isEmpty()){
            errorMsg += "You need to add Tags";
            checkPass[0] = false;
        }
        if (errorMsg.isEmpty()==false){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setMessage(errorMsg)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkPass[0] = true;
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if(checkPass[0] == true){
            ArrayList<String> requirement = new ArrayList<>();
            ArrayList<String> tag = new ArrayList<>();

            requirement.add(etRequirement.getText().toString());
            if(row2.getVisibility()==View.VISIBLE){
                requirement.add(etRequirement2.getText().toString());
            }
            if(row3.getVisibility()==View.VISIBLE){
                requirement.add(etRequirement3.getText().toString());
            }
            if(row4.getVisibility()==View.VISIBLE){
                requirement.add(etRequirement4.getText().toString());
            }
            if(row5.getVisibility()==View.VISIBLE){
                requirement.add(etRequirement5.getText().toString());
            }

            if(!spinnerTag1.getSelectedItem().toString().trim().isEmpty()) tag.add(spinnerTag1.getSelectedItem().toString().trim());
            if(!spinnerTag2.getSelectedItem().toString().trim().isEmpty()) tag.add(spinnerTag2.getSelectedItem().toString().trim());
            if(!spinnerTag3.getSelectedItem().toString().trim().isEmpty()) tag.add(spinnerTag3.getSelectedItem().toString().trim());

            HashMap<String,String> hashTag = new HashMap<>();
            HashMap<String,String> hashRequirement = new HashMap<>();

            for (int i=0; i< requirement.size(); i++){
                hashRequirement.put("requirement"+i,requirement.get(i));
            }
            for (int i=0; i< tag.size(); i++){
                hashTag.put("tag"+i,tag.get(i));
            }

            String post = spinnerPost.getSelectedItem().toString().trim();
            String id = UUID.randomUUID().toString();
            JobPost jobPost = new JobPost("false",id,title,email,post,hashTag,hashRequirement,hashTag.size(),hashRequirement.size(),tag,salary,summary);
            db.collection("employer").whereEqualTo("email",email).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                            DocumentSnapshot document = snapshots.get(0);
                            String company = document.getString("name").toString();
                            String phone = document.getString("phone").toString();
                            String address = document.getString("address").toString();
                            Toast.makeText(AddJobActivity.this, company+"\n"+phone+"\n"+address, Toast.LENGTH_SHORT).show();
                            jobPost.setCompany(company);
                            jobPost.setPhone(phone);
                            jobPost.setAddress(address);
                        }
                    });

            AlertDialog.Builder builder = new AlertDialog.Builder(AddJobActivity.this);
            builder.setMessage("title : "+title+"\n"+
                            "email : "+email+"\n"+
                            "post : "+ jobPost.getPost() +"\n"+
                            "tag : "+hashTag.toString()+"\n"+
                            "requirement : "+hashRequirement.toString()+"\n"+
                            "salary : "+salary+"\n"+
                            "summary : "+summary+"\n")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProgressDialog progressDialog = new ProgressDialog(AddJobActivity.this);
                            progressDialog.setTitle("Uploading file...");
                            progressDialog.show();
                            db.collection("job post").document(jobPost.getID()).set(jobPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddJobActivity.this, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            builder.show();

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getTag().toString().equals("add")){
           AddRequirement(v);
        } else if(v.getTag().toString().equals("remove2") || v.getTag().toString().equals("remove3") || v.getTag().toString().equals("remove4") || v.getTag().toString().equals("remove5")){
             RemoveRequirement(v);
        }
    }

    private void AddRequirement(View v) {
        if(row2.getVisibility() == View.GONE){
            row2.setVisibility(View.VISIBLE);
        } else if(row3.getVisibility() == View.GONE){
            row3.setVisibility(View.VISIBLE);
        } else if(row4.getVisibility() == View.GONE){
            row4.setVisibility(View.VISIBLE);
        } else if(row5.getVisibility() == View.GONE){
            row5.setVisibility(View.VISIBLE);
        } else{
            //do nothing
        }
    }

    private void RemoveRequirement(View v) {
        if(v.getTag().toString().equals("remove2")){
            row2.setVisibility(View.GONE);
            etRequirement2.setText("");
        } else if(v.getTag().toString().equals("remove3")){
            row3.setVisibility(View.GONE);
            etRequirement3.setText("");
        } else if(v.getTag().toString().equals("remove4")){
            row4.setVisibility(View.GONE);
            etRequirement4.setText("");
        } else if(v.getTag().toString().equals("remove5")){
            row5.setVisibility(View.GONE);
            etRequirement5.setText("");
        }
    }
}