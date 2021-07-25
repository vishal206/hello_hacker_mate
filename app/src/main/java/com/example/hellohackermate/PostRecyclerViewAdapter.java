package com.example.hellohackermate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyViewHolder> {


    ArrayList<ModelPost> modelPost;

    public PostRecyclerViewAdapter(ArrayList<ModelPost> modelPost) {
        this.modelPost = modelPost;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerViewAdapter.MyViewHolder holder, int position) {
        String title=modelPost.get(position).getTitle();
        String desc=modelPost.get(position).getDescription();
        String Name=modelPost.get(position).getUname();
        System.out.println("----------3");
        System.out.println(title);
        holder.pTitle.setText(title);
        holder.pDesc.setText(desc);
        holder.uName.setText(Name);
    }

    @Override
    public int getItemCount() {
        return modelPost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView pTitle,pDesc,pLink,uName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pTitle=itemView.findViewById(R.id.txt_pTitle);
            pDesc=itemView.findViewById(R.id.txt_pDesc);
            uName=itemView.findViewById(R.id.txt_pUname);
        }
    }
}
