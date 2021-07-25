package com.example.hellohackermate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileHackerFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    DocumentReference docref;
    ImageView avatartv;
    TextView uName;
    Button btn_addEvent,btn_editProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_hacker, container, false);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=firebaseAuth.getCurrentUser();
        String uid=firebaseUser.getUid();
        firebaseFirestore=FirebaseFirestore.getInstance();
        docref=FirebaseFirestore.getInstance().collection("Users").document(uid);

        avatartv=view.findViewById(R.id.Hackerprofile_pic);
        uName=view.findViewById(R.id.txt_HackeruName);
        btn_editProfile=view.findViewById(R.id.btn_HackereditProfile);

        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        uName.setText(doc.getString("name"));
                    }
                    else{
                        Toast.makeText(getActivity(), "error in finding uName", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
}