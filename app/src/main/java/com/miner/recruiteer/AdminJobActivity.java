package com.miner.recruiteer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdminJobActivity extends AppCompatActivity implements ItemClickListener {

    RecyclerView rvJobPosts;
    ArrayList<JobPost> jobPostsArrayList;
    JobPostAdapter jobPostAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_job);

        email = getIntent().getStringExtra("email");
        rvJobPosts =findViewById(R.id.rvAdminJobPosts);
        rvJobPosts.setHasFixedSize(true);
        rvJobPosts.setLayoutManager(new LinearLayoutManager(this));

        jobPostsArrayList = new ArrayList<>();
        jobPostAdapter = new JobPostAdapter(this,jobPostsArrayList,this);
        rvJobPosts.setAdapter(jobPostAdapter);

        FetchData();
    }

    private void FetchData() {
        db.collection("job post").whereEqualTo("accepted","false").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot :queryDocumentSnapshots.getDocuments()){
                            JobPost jobPost = new JobPost();
                            jobPost.setID(snapshot.getString("id"));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("title : "+jobPost.getTitle()+"\n"+
                        "email : "+jobPost.getEmail()+"\n"+
                        "post : "+ jobPost.getPost() +"\n"+
                        "tag : "+jobPost.getTagArray()+"\n"+
                        "requirement : "+jobPost.getRequirements()+"\n"+
                        "salary : "+jobPost.getSalary()+"\n"+
                        "summary : "+jobPost.getSummary()+"\n")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("job post").document(jobPost.getID().trim()).update("accepted","true").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminJobActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("job post").document(jobPost.getID().trim()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminJobActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}