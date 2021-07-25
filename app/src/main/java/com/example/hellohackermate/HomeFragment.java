
package com.example.hellohackermate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private ArrayList<ModelPost> modelPosts;
    private RecyclerView recyclerView;
    PostRecyclerViewAdapter postRecyclerViewAdapter;
    private int i;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);


        modelPosts=new ArrayList<>();
        recyclerView=view.findViewById(R.id.home_recyclerView);
        i=3;

//        setPostInfo();
        FirebaseFirestore.getInstance().collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("OUTPUT::", document.getId() + " => " + document.getData());
//                        String id=document.getId();
                        ModelPost mp=new ModelPost(document.getString("uid"),document.getString("uname"),document.getString("phone"),document.getString("link"),document.getString("description"),
                                document.getString("pinterested"),document.getString("title"),document.getString("ptime"));

//
                        modelPosts.add(mp);
//                        modelPosts.add(new ModelPost(document.getString("uid"),document.getString("uname"),document.getString("phone"),document.getString("link"),document.getString("description"),
//                                document.getString("pinterested"),document.getString("title"),document.getString("ptime")));


//                        System.out.println("------------1");
//                        System.out.println(modelPosts.get(0).getTitle());

                    }
                    System.out.println("------------2");
                    System.out.println(modelPosts.size());
                    postRecyclerViewAdapter=new PostRecyclerViewAdapter(modelPosts);
                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(postRecyclerViewAdapter);
                } else {
                    Log.d("OUTPUT::", "Error getting documents: ", task.getException());
                }
            }
        });

        System.out.println(i);
//        setAdapter();


        return view;
    }


    private void setAdapter() {
        postRecyclerViewAdapter=new PostRecyclerViewAdapter(modelPosts);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postRecyclerViewAdapter);
    }

    private void setPostInfo() {
        FirebaseFirestore.getInstance().collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("OUTPUT::", document.getId() + " => " + document.getData());
//                        String id=document.getId();
                        ModelPost mp=new ModelPost(document.getString("uid"),document.getString("uname"),document.getString("phone"),document.getString("link"),document.getString("description"),
                                document.getString("pinterested"),document.getString("title"),document.getString("ptime"));

//
//                        modelPosts.add(mp);
//                        modelPosts.add(new ModelPost(document.getString("uid"),document.getString("uname"),document.getString("phone"),document.getString("link"),document.getString("description"),
//                                document.getString("pinterested"),document.getString("title"),document.getString("ptime")));


//                        System.out.println("------------1");
//                        System.out.println(modelPosts.get(0).getTitle());

                    }
                } else {
                    Log.d("OUTPUT::", "Error getting documents: ", task.getException());
                }
            }
        });


//        modelPosts.add(new ModelPost())
    }
}