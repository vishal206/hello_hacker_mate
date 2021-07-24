package com.example.hellohackermate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth=FirebaseAuth.getInstance();
        if(mAuth!=null){
            currentUser= mAuth.getCurrentUser();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user=mAuth.getCurrentUser();

                if(user==null){
                    startActivity(new Intent(SplashScreen.this,RegisterActivity.class));
                    finish();
                }else{

                    String uid= user.getUid();
                    DocumentReference docref= FirebaseFirestore.getInstance().collection("Users").document(uid);
                    docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc=task.getResult();
                                if(doc.exists()){
                                    if(doc.getString("userType").equals("host")) {
                                        Intent mainIntent = new Intent(SplashScreen.this, dashBoardHostActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                    else if(doc.getString("userType").equals("hacker")){
                                        Intent mainIntent = new Intent(SplashScreen.this, dashBoardHacker.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                    else
                                        Toast.makeText(SplashScreen.this, "error in userType", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });


                }
            }
        },1000);
    }
}