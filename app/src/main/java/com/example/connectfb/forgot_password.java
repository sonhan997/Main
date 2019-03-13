package com.example.connectfb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText userEmail;
    Button userPass;

    FirebaseAuth mAuth;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        progressBar = findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.btnForgotPass);



        mAuth = FirebaseAuth.getInstance();


        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(userEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(forgot_password.this,
                                            "Password send to your email", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(forgot_password.this,
                                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }



    }

