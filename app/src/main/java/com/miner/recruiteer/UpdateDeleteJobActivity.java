package com.miner.recruiteer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.miner.recruiteer.Adapter.ItemClickListener;
import com.miner.recruiteer.Adapter.JobPostAdapter;
import com.miner.recruiteer.Class.JobPost;
import com.miner.recruiteer.Class.UpdateJobPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateDeleteJobActivity extends AppCompatActivity implements ItemClickListener,View.OnClickListener {

    RecyclerView rvCheckDelete;
    ArrayList<JobPost> jobPostsArrayList;
    JobPostAdapter jobPostAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;

    LinearLayout row2Update,row3Update,row4Update,row5Update;
    EditText etUpdateTitle,etUpdateSalary,etUpdateRequirement,etUpdateRequirement2,etUpdateRequirement3,etUpdateRequirement4,etUpdateRequirement5,etUpdateSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_and_delete_job);

        email = getIntent().getStringExtra("email");
        rvCheckDelete = findViewById(R.id.rvCheckDelete);
        rvCheckDelete.setHasFixedSize(true);
        rvCheckDelete.setLayoutManager(new LinearLayoutManager(this));

        jobPostsArrayList = new ArrayList<>();
        jobPostAdapter = new JobPostAdapter(this,jobPostsArrayList,this);
        rvCheckDelete.setAdapter(jobPostAdapter);

        FetchData();
    }

    private void FetchData() {

        db.collection("job post").whereEqualTo("email",email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(UpdateDeleteJobActivity.this, "There is no Job posts you created", Toast.LENGTH_SHORT).show();
                        }
                        for(DocumentSnapshot snapshot :queryDocumentSnapshots.getDocuments()){
                            JobPost jobPost = new JobPost();
                            jobPost.setAccepted(snapshot.getString("accepted"));
                            jobPost.setID(snapshot.getString("id"));
                            jobPost.setCompany(snapshot.getString("company"));
                            jobPost.setTitle(snapshot.getString("title"));
                            jobPost.setEmail(snapshot.getString("email"));
                            jobPost.setPhone(snapshot.getString("phone"));
                            jobPost.setAddress(snapshot.getString("address"));
                            jobPost.setPost(snapshot.getString("post"));
                            ArrayList<String> tag = new ArrayList<>();
                            HashMap<String,String> hashTag = new HashMap<>();
                            HashMap<String,String> requirements = new HashMap<>();
                            jobPost.setTotalTags(Math.toIntExact(snapshot.getLong("totalTags")));
                            jobPost.setTotalRequirements(Math.toIntExact(snapshot.getLong("totalRequirements")));
                            jobPost.setSalary(snapshot.getString("salary"));
                            jobPost.setSummary(snapshot.getString("summary"));
                            hashTag.putAll((Map<? extends String, ? extends String>) snapshot.get("tags"));
                            for(int i=0; i<hashTag.size();i++){
                                tag.add(hashTag.get("tag"+i));
                            }
                            jobPost.setTagArray(tag);
                            requirements.putAll((Map<? extends String, ? extends String>) snapshot.get("requirements"));
                            jobPost.setRequirements(requirements);
                            jobPostsArrayList.add(jobPost);
                        }
                        jobPostAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemClick(JobPost jobPost) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("title : "+jobPost.getTitle()+"\n"+
                        "email : "+jobPost.getEmail()+"\n"+
                        "post : "+ jobPost.getPost() +"\n"+
                        "tag : "+jobPost.getTagArray()+"\n"+
                        "requirement : "+jobPost.getRequirements()+"\n"+
                        "salary : "+jobPost.getSalary()+"\n"+
                        "summary : "+jobPost.getSummary()+"\n")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UpdateDialog(jobPost);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("job post").document(jobPost.getID().trim()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(UpdateDeleteJobActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(UpdateDeleteJobActivity.this, "Failed to deleted.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void UpdateDialog(JobPost jobPost){

        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(UpdateDeleteJobActivity.this);
        View v = LayoutInflater.from(UpdateDeleteJobActivity.this).inflate(R.layout.update_layout,findViewById(R.id.updateLayout));
        updateBuilder.setView(v);
        updateBuilder.setCancelable(false);
        Spinner spinnerUpdateTag1,spinnerUpdateTag2,spinnerUpdateTag3,spinnerUpdatePost;
        Button btnUpdateAddRequirement,btnUpdateAddRequirement2,btnUpdateAddRequirement3,btnUpdateAddRequirement4,btnUpdateAddRequirement5;
        Button btnUpdateRemoveRequirement2,btnUpdateRemoveRequirement3,btnUpdateRemoveRequirement4,btnUpdateRemoveRequirement5;
        Button btnUpdatePost;

        row2Update = v.findViewById(R.id.row2Update);
        row3Update = v.findViewById(R.id.row3Update);
        row4Update = v.findViewById(R.id.row4Update);
        row5Update = v.findViewById(R.id.row5Update);

        etUpdateTitle = v.findViewById(R.id.etUpdateJobTitle);
        etUpdateTitle.setText(jobPost.getTitle());
        etUpdateSalary = v.findViewById(R.id.etUpdateJobSalary);
        etUpdateSalary.setText(jobPost.getSalary());
        etUpdateRequirement = v.findViewById(R.id.etUpdateRequirement);
        etUpdateRequirement2 = v.findViewById(R.id.etUpdateRequirement2);
        etUpdateRequirement3 = v.findViewById(R.id.etUpdateRequirement3);
        etUpdateRequirement4 = v.findViewById(R.id.etUpdateRequirement4);
        etUpdateRequirement5 = v.findViewById(R.id.etUpdateRequirement5);
        etUpdateSummary = v.findViewById(R.id.etUpdateJobSummary);
        etUpdateSummary.setText(jobPost.getSummary());

        for(int i=0;i<jobPost.getRequirements().size()+1;i++){
            switch (i){
                case 1: etUpdateRequirement.setText(jobPost.getRequirements().get("requirement0"));
                    break;
                case 2: etUpdateRequirement2.setText(jobPost.getRequirements().get("requirement1"));
                    row2Update.setVisibility(View.VISIBLE);
                    break;
                case 3: etUpdateRequirement3.setText(jobPost.getRequirements().get("requirement2"));
                    row3Update.setVisibility(View.VISIBLE);
                    break;
                case 4: etUpdateRequirement4.setText(jobPost.getRequirements().get("requirement3"));
                    row4Update.setVisibility(View.VISIBLE);
                    break;
                case 5: etUpdateRequirement5.setText(jobPost.getRequirements().get("requirement4"));
                    row5Update.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        spinnerUpdatePost = v.findViewById(R.id.spinnerUpdatePost);
        spinnerUpdateTag1= v.findViewById(R.id.spinnerUpdateTag1);
        spinnerUpdateTag2 = v.findViewById(R.id.spinnerUpdateTag2);
        spinnerUpdateTag3= v.findViewById(R.id.spinnerUpdateTag3);

        ArrayList<String> reservePost = new ArrayList<>();
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
        spinnerUpdatePost.setAdapter(spinnerPostAdapter);

        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(this,R.array.tag, android.R.layout.simple_spinner_item);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdateTag1.setAdapter(tagAdapter);
        spinnerUpdateTag2.setAdapter(tagAdapter);
        spinnerUpdateTag3.setAdapter(tagAdapter);

        btnUpdateAddRequirement = v.findViewById(R.id.btnUpdateAddRequirement);
        btnUpdateAddRequirement2 = v.findViewById(R.id.btnUpdateAddRequirement2);
        btnUpdateAddRequirement3 = v.findViewById(R.id.btnUpdateAddRequirement3);
        btnUpdateAddRequirement4 = v.findViewById(R.id.btnUpdateAddRequirement4);
        btnUpdateAddRequirement5 = v.findViewById(R.id.btnUpdateAddRequirement5);
        btnUpdateAddRequirement.setOnClickListener(this);
        btnUpdateAddRequirement2.setOnClickListener(this);
        btnUpdateAddRequirement3.setOnClickListener(this);
        btnUpdateAddRequirement4.setOnClickListener(this);
        btnUpdateAddRequirement5.setOnClickListener(this);

        btnUpdateRemoveRequirement2 = v.findViewById(R.id.btnUpdateRemoveRequirement2);
        btnUpdateRemoveRequirement3 = v.findViewById(R.id.btnUpdateRemoveRequirement3);
        btnUpdateRemoveRequirement4 = v.findViewById(R.id.btnUpdateRemoveRequirement4);
        btnUpdateRemoveRequirement5 = v.findViewById(R.id.btnUpdateRemoveRequirement5);
        btnUpdateRemoveRequirement2.setOnClickListener(this);
        btnUpdateRemoveRequirement3.setOnClickListener(this);
        btnUpdateRemoveRequirement4.setOnClickListener(this);
        btnUpdateRemoveRequirement5.setOnClickListener(this);

        btnUpdatePost = v.findViewById(R.id.btnUpdatePost);
        btnUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Boolean[] checkPass = {true};
                UpdateJobPost update = new UpdateJobPost();
                String errorMsg="";
                String title = etUpdateTitle.getText().toString().trim();
                String salary = etUpdateSalary.getText().toString().trim();
                String summary = etUpdateSummary.getText().toString().trim();

                if(spinnerUpdatePost.getSelectedItem().toString().trim().equals(reservePost.get(0))){
                    errorMsg += "You need to select posts.\n";
                    checkPass[0] = false;
                }

                if(title.isEmpty()) {
                    etUpdateTitle.setError("You need to add Job Title.");
                    checkPass[0] = false;
                }
                if(salary.isEmpty()){
                    etUpdateSalary.setError("You need to add Salary");
                    checkPass[0] = false;
                }
                if(summary.isEmpty()){
                    etUpdateSummary.setError("You need to add Summary");
                    checkPass[0] = false;
                }
                if(etUpdateRequirement.getText().toString().trim().isEmpty()){
                    etUpdateRequirement.setError("You need to add requirement");
                    checkPass[0] = false;
                }
                if(spinnerUpdateTag1.getSelectedItem().toString().trim().isEmpty() && spinnerUpdateTag2.getSelectedItem().toString().trim().isEmpty() && spinnerUpdateTag3.getSelectedItem().toString().trim().isEmpty()){
                    errorMsg += "You need to add Tags";
                    checkPass[0] = false;
                }
                if (errorMsg.isEmpty()==false){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateDeleteJobActivity.this);
                    builder.setCancelable(false)
                            .setMessage(errorMsg)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkPass[0] = true;
                                    dialog.dismiss();
                                }
                            });
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                };

                if(checkPass[0] == true){
                    ArrayList<String> requirement = new ArrayList<>();
                    ArrayList<String> tag = new ArrayList<>();

                    requirement.add(etUpdateRequirement.getText().toString());
                    if(row2Update.getVisibility()==View.VISIBLE){
                        requirement.add(etUpdateRequirement2.getText().toString());
                    }
                    if(row3Update.getVisibility()==View.VISIBLE){
                        requirement.add(etUpdateRequirement3.getText().toString());
                    }
                    if(row4Update.getVisibility()==View.VISIBLE){
                        requirement.add(etUpdateRequirement4.getText().toString());
                    }
                    if(row5Update.getVisibility()==View.VISIBLE){
                        requirement.add(etUpdateRequirement5.getText().toString());
                    }

                    if(!spinnerUpdateTag1.getSelectedItem().toString().trim().isEmpty()) tag.add(spinnerUpdateTag1.getSelectedItem().toString().trim());
                    if(!spinnerUpdateTag2.getSelectedItem().toString().trim().isEmpty()) tag.add(spinnerUpdateTag2.getSelectedItem().toString().trim());
                    if(!spinnerUpdateTag3.getSelectedItem().toString().trim().isEmpty()) tag.add(spinnerUpdateTag3.getSelectedItem().toString().trim());

                    HashMap<String,String> hashTag = new HashMap<>();
                    HashMap<String,String> hashRequirement = new HashMap<>();

                    for (int i=0; i< requirement.size(); i++){
                        hashRequirement.put("requirement"+i,requirement.get(i));
                    }
                    for (int i=0; i< tag.size(); i++){
                        hashTag.put("tag"+i,tag.get(i));
                    }

                    update.setRequirements(hashRequirement);
                    update.setTags(hashTag);
                    update.setTagArray(tag);
                    update.setTotalRequirements(hashRequirement.size());
                    update.setTotalTags(hashTag.size());

                    update.setTitle(title);
                    update.setSalary(salary);
                    update.setSummary(summary);
                    update.setPost(spinnerUpdatePost.getSelectedItem().toString().trim());

                    db.collection("job post").document(jobPost.getID().trim()).set(update, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UpdateDeleteJobActivity.this, "Updates Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            }
        });

        AlertDialog updateDialog = updateBuilder.create();
        updateDialog.show();
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
        if(row2Update.getVisibility() == View.GONE){
            row2Update.setVisibility(View.VISIBLE);
        } else if(row3Update.getVisibility() == View.GONE){
            row3Update.setVisibility(View.VISIBLE);
        } else if(row4Update.getVisibility() == View.GONE){
            row4Update.setVisibility(View.VISIBLE);
        } else if(row5Update.getVisibility() == View.GONE){
            row5Update.setVisibility(View.VISIBLE);
        } else{
            //do nothing
        }
    }

    private void RemoveRequirement(View v) {
        if(v.getTag().toString().equals("remove2")){
            row2Update.setVisibility(View.GONE);
            etUpdateRequirement2.setText("");
        } else if(v.getTag().toString().equals("remove3")){
            row3Update.setVisibility(View.GONE);
            etUpdateRequirement3.setText("");
        } else if(v.getTag().toString().equals("remove4")){
            row4Update.setVisibility(View.GONE);
            etUpdateRequirement4.setText("");
        } else if(v.getTag().toString().equals("remove5")){
            row5Update.setVisibility(View.GONE);
            etUpdateRequirement5.setText("");
        }
    }
}