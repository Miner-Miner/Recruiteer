package com.miner.recruiteer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.miner.recruiteer.Class.Profile;
import com.miner.recruiteer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder>{
    Context context;
    ArrayList<Profile> profileArrayList;
    String type;
    private ProfileClickListener profileClickListener;

    public ProfileAdapter(Context context, ArrayList<Profile> profileArrayList, String type, ProfileClickListener profileClickListener) {
        this.context = context;
        this.profileArrayList = profileArrayList;
        this.type = type;
        this.profileClickListener = profileClickListener;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.profile,parent,false);
        return new ProfileAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.MyViewHolder holder, int position) {
        holder.tvUserName.setText(profileArrayList.get(position).getName());
        String imageUri = profileArrayList.get(position).getEmail().replace("@","-");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imageRef = storageReference.child(type+"/"+imageUri.replace(".","-"));

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(context, "Successfully get URI", Toast.LENGTH_SHORT).show();
                Picasso.get()
                        .load(uri)
                        .fit()
                        .centerInside()
                        .into(holder.imageUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to get uri", Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvUserEmail.setText(profileArrayList.get(position).getEmail());
        holder.tvUserPhone.setText(profileArrayList.get(position).getPhone());
        holder.tvUserAddress.setText(profileArrayList.get(position).getAddress());
        holder.cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileClickListener.onItemClick(profileArrayList.get(position),type);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardProfile;
        TextView tvUserName,tvUserEmail,tvUserPhone,tvUserAddress;
        ImageView imageUser;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            tvUserAddress = itemView.findViewById(R.id.tvUserAddress);
            imageUser = itemView.findViewById(R.id.imageUser);
            cardProfile = itemView.findViewById(R.id.cardProfile);
        }
    }
}
