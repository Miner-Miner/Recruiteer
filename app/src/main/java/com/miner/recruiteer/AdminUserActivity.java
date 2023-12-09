package com.miner.recruiteer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.miner.recruiteer.Adapter.ProfileAdapter;
import com.miner.recruiteer.Adapter.ProfileClickListener;
import com.miner.recruiteer.Class.Profile;

import java.util.ArrayList;

public class AdminUserActivity extends AppCompatActivity implements ProfileClickListener {

    Spinner spinnerUser;
    RecyclerView rvUsers;
    ArrayList<Profile> profileArrayList;
    ProfileAdapter profileAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        spinnerUser = findViewById(R.id.spinnerUser);
        rvUsers = findViewById(R.id.rvUsers);

        ArrayList<String> user = new ArrayList<>();
        user.add("employer");
        user.add("employee");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, user);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(spinnerAdapter);

        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        profileArrayList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this,profileArrayList, spinnerUser.getSelectedItem().toString(),this);
        rvUsers.setAdapter(profileAdapter);

        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profileArrayList.clear();
                FetchData(spinnerUser.getSelectedItem().toString());
                profileAdapter.setType(spinnerUser.getSelectedItem().toString());
                rvUsers.setAdapter(profileAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void FetchData(String tag) {
        db.collection(tag).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(AdminUserActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            for(DocumentSnapshot snapshot :queryDocumentSnapshots.getDocuments()){
                                Profile profile = new Profile();
                                profile.setId(snapshot.getString("id"));
                                profile.setEmail(snapshot.getString("email"));
                                profile.setAddress(snapshot.getString("address"));
                                profile.setPhone(snapshot.getString("phone"));
                                profile.setName(snapshot.getString("name"));
                                profileArrayList.add(profile);
                            }
                            profileAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(Profile profile,String user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminUserActivity.this);
        builder.setCancelable(false)
                .setTitle("Delete")
                .setMessage("Do you want to delete user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ArrayList<String> id = new ArrayList<>();
                        db.collection("job post").whereEqualTo("email",profile.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(DocumentSnapshot snapshot: queryDocumentSnapshots){
                                    id.add(snapshot.getString("id"));
                                }
                            }
                        });
                        WriteBatch batch = db.batch();
                        for (int i=0; i<id.size(); i++){
                            batch.delete(db.collection("job post").document(id.get(i)));
                        }
                        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminUserActivity.this, "Deleted job posts", Toast.LENGTH_SHORT).show();
                            }
                        });
                        db.collection(user).document(profile.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminUserActivity.this, "Successfully deleted account.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //remove data from rldb
                        /*
                        reference.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            }
                        });
                        */
                        profileArrayList.clear();
                        FetchData(spinnerUser.getSelectedItem().toString());
                        rvUsers.setAdapter(profileAdapter);
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