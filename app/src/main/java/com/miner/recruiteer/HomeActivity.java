package com.miner.recruiteer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miner.recruiteer.Adapter.ItemClickListener;
import com.miner.recruiteer.Adapter.JobPostAdapter;
import com.miner.recruiteer.Class.JobPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements ItemClickListener {

    Spinner spinnerTags;
    LottieAnimationView lottieHome;
    RecyclerView rvJobPosts;
    ArrayList<JobPost> jobPostsArrayList;
    JobPostAdapter jobPostAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = getIntent().getStringExtra("email");

        rvJobPosts =findViewById(R.id.rvJobPosts);
        spinnerTags = findViewById(R.id.spinnerTags);
        lottieHome = findViewById(R.id.lottieHome);
        rvJobPosts.setHasFixedSize(true);
        rvJobPosts.setLayoutManager(new LinearLayoutManager(this));

        jobPostsArrayList = new ArrayList<>();
        jobPostAdapter = new JobPostAdapter(this,jobPostsArrayList,this);
        rvJobPosts.setAdapter(jobPostAdapter);

        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(this,R.array.tag, R.layout.spinner_item);
        tagAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTags.setAdapter(tagAdapter);
        spinnerTags.setSelection(1);
        //lottieHome.setAnimation();
        spinnerTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobPostsArrayList.clear();
                FetchData(spinnerTags.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void FetchData(String tag) {
        db.collection("job post").whereEqualTo("accepted","true").whereArrayContains("tagArray",tag).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot :queryDocumentSnapshots.getDocuments()){
                            JobPost jobPost = new JobPost();
                            jobPost.setAccepted(snapshot.getString("accepted"));
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
        //Toast.makeText(this, jobPost.getTagArray().toString(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,JobDescriptionActivity.class).putExtra("data", jobPost)
                .putExtra("email",email));
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