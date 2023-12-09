package com.miner.recruiteer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.miner.recruiteer.Class.JobPost;
import com.miner.recruiteer.R;

import java.util.ArrayList;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.MyViewHolder> {
    Context context;
    ArrayList<JobPost> jobPosts;
    private ItemClickListener listener;

    public JobPostAdapter(Context context,ArrayList<JobPost> jobPosts, ItemClickListener listener){
        this.context = context;
        this.jobPosts = jobPosts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.jobpost,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobPostAdapter.MyViewHolder holder, int position) {
        JobPost post = jobPosts.get(position);

        holder.tvDes.setText(post.getTitle());
        holder.tvPos.setText(post.getPost());
        holder.tvSal.setText(post.getSalary());
        holder.cardJobPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(jobPosts.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobPosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDes,tvPos,tvSal;
        CardView cardJobPost;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvPos = itemView.findViewById(R.id.tvPos);
            tvSal = itemView.findViewById(R.id.tvSal);
            cardJobPost = itemView.findViewById(R.id.cardJobPost);

        }
    }
}
