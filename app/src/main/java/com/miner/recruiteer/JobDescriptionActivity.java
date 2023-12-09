package com.miner.recruiteer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.miner.recruiteer.Adapter.TagAdapter;
import com.miner.recruiteer.Class.JobPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JobDescriptionActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvTitle,tvCompany,tvPost,tvSalary,tvSummary,tvRequriement,tvEmail,tvPhone;
    RecyclerView rvTags;
    TagAdapter tagAdapter;
    ImageView imageCompany;
    JobPost jobPost;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_description);
        jobPost = (JobPost) getIntent().getSerializableExtra("data");
        InitializeViews();
    }

    private void InitializeViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvPost = (TextView) findViewById(R.id.tvPost);
        tvSalary = (TextView) findViewById(R.id.tvSalary);
        tvSummary = (TextView) findViewById(R.id.tvSummary);
        tvRequriement = (TextView) findViewById(R.id.tvRequirement);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        imageCompany = findViewById(R.id.imageCompany);
        storageReference = FirebaseStorage.getInstance().getReference();
        String imageUri = jobPost.getEmail().replace("@","-");
        final StorageReference imageRef = storageReference.child("employer/"+imageUri.replace(".","-"));

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(JobDescriptionActivity.this, "Successfully get URI", Toast.LENGTH_SHORT).show();
                Picasso.get()
                        .load(uri)
                        .fit()
                        .centerInside()
                        .into(imageCompany);
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(JobDescriptionActivity.this, "Failed to get uri", Toast.LENGTH_SHORT).show();
           }
       });
        rvTags = findViewById(R.id.rvTags);
        rvTags.setHasFixedSize(true);
        rvTags.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        ArrayList<String> tags = new ArrayList<>();
        for (int i=0; i<jobPost.getTagArray().size();i++){
            tags.add(jobPost.getTagArray().get(i));
        }
        tagAdapter = new TagAdapter(this,tags);
        rvTags.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
        tvTitle.setText(jobPost.getTitle());
        tvCompany.setText(jobPost.getCompany());
        tvPost.setText(jobPost.getPost());
        tvSalary.setText(jobPost.getSalary());
        tvSummary.setText(jobPost.getSummary());
        String Requirement = "";
        for(int i=0; i<jobPost.getRequirements().size();i++){
            Requirement += "Requirement "+(i+1)+": "+jobPost.getRequirements().get("requirement"+i).toString().trim()+"\n";
        }
        tvRequriement.setText(Requirement.trim());
        tvEmail.setText(jobPost.getEmail());
        tvEmail.setOnClickListener(this);
        tvPhone.setText(jobPost.getPhone());
        tvPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag().toString().equals("phone")){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tvPhone.getText().toString().trim(), null));
            startActivity(intent);
        }else{
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"+tvEmail.getText().toString().trim()));
            startActivity(Intent.createChooser(emailIntent, "Applying Job"));
            Toast.makeText(this, tvEmail.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}