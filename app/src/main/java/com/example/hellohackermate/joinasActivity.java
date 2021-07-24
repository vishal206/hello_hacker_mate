package com.example.hellohackermate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class joinasActivity extends AppCompatActivity {

    private Button btn_host,btn_hacker;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinas);

        btn_hacker=findViewById(R.id.btn_hacker);
        btn_host=findViewById(R.id.btn_host);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();
        String uid= user.getUid();
        DocumentReference ref= FirebaseFirestore.getInstance().collection("Users").document(uid);


        btn_hacker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(joinasActivity.this, "activity not yet done", Toast.LENGTH_SHORT).show();
                ref.update("userType","hacker");
                Intent i=new Intent(joinasActivity.this, dashBoardHacker.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        btn_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.update("userType","host");
                Intent i=new Intent(joinasActivity.this, dashBoardHostActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }
}