package com.example.hellohackermate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtPhone,edtOtp,edtUname;
    private Button btn_getOtp,btn_signUp;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        edtPhone=findViewById(R.id.edt_phone);
        edtOtp=findViewById(R.id.edt_otp);
        edtUname=findViewById(R.id.edt_username);
        btn_getOtp=findViewById(R.id.btn_requestOtp);
        btn_signUp=findViewById(R.id.btn_signUp);


        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edtPhone.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "enter a valid phone number", Toast.LENGTH_SHORT).show();
                }else{
                    String phone="+91"+edtPhone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtOtp.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "please enter OTP", Toast.LENGTH_SHORT).show();
                }else{
                    verifyCode(edtOtp.getText().toString());
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i=new Intent(RegisterActivity.this,DashBoardActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationCode(String phone) {;
        PhoneAuthOptions options= PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code =phoneAuthCredential.getSmsCode();
            if(code!=null){
                edtOtp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
            signInWithCredential(credential);
    }

}
