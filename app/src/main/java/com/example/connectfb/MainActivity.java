package com.example.connectfb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewDK;
    private Button btnDangKi,btnDangNhap;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebase;
    private TextInputLayout textInputEmail,textInputNAME,textInputPassword, textInputConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
         mAuth = FirebaseAuth.getInstance();
         Anhxa();
        btnDangKi.setOnClickListener(this);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    private void DangkiRealtime(String username,String email,String password,String cfpassword){
       /* String username = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();
        String cfpassword = editTextConfirm.getText().toString().trim();*/
        User newUser = new User(username,email,password,cfpassword);
        mDatabase.child("UserID").push().setValue(newUser);
    }
    public void Anhxa(){
        btnDangKi = findViewById(R.id.btnDangki);
        btnDangNhap=findViewById(R.id.btnDangNhap);
        textInputNAME = findViewById(R.id.textInput_NAME);
        textInputEmail = findViewById(R.id.textInput_Email);
        textInputPassword=findViewById(R.id.textInput_Password);
        textInputConfirmPassword=findViewById(R.id.textInputConfirm_Password);

    }
    private  void Dangki(){
        String email = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Pass", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Register User...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Registered successfully. Please check your email for verification",
                                        Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this,  task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override

    public void onClick(View v) {
        Validate validate = new Validate();

        String username = textInputNAME.getEditText().getText().toString().trim();
        String email = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        String cfpassword = textInputConfirmPassword.getEditText().getText().toString().trim();
        if(v==btnDangKi){
            if(validate.validateEmail(email,textInputEmail) && validate.validateUsername(username,textInputNAME)
                    && validate.validatePassword(password, textInputPassword)
                    && validate.validateConfirmPassword(password,cfpassword,textInputConfirmPassword)){
                Dangki();
                DangkiRealtime(username,email,password,cfpassword);
                return;
            }
//            String input = "FullName: " + textInputNAME.getEditText().getText().toString();
//            input += "\n";
//            input += "Email " + textInputEmail.getEditText().getText().toString();
//            input += "\n";
//            input += "Password: " + textInputPassword.getEditText().getText().toString();
//            input += "\n";
//            input +="Confirm Password: " + textInputConfirmPassword.getEditText().getText().toString();
//            Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        }

    }
}
