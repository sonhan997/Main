package com.example.connectfb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewDK;
    private Button btnDangNhap,btnForgot;
    private SignInButton btnGoogleButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebase;
    private TextInputLayout textInputEmail,textInputPassword;
    private GoogleSignInClient mGoogle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Anhxa();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogle = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        btnDangNhap.setOnClickListener(this);
        findViewById(R.id.btnGoogle).setOnClickListener(this);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,forgot_password.class));
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, profile.class));


                            Toast.makeText(getApplicationContext(), "USER LOGGED IN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "COULD NOT LOG IN USER", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    public void Anhxa(){
        btnDangNhap = findViewById(R.id.btnDang_nhap);
        btnForgot=findViewById(R.id.btnforgot);
        btnGoogleButton=(SignInButton) findViewById(R.id.btnGoogle);
        textInputEmail = findViewById(R.id.textInput_Email);
        textInputPassword=findViewById(R.id.textInput_Password);
    }
    private void DangNhap(){
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
        progressDialog.setMessage("LOGIN");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(LoginActivity.this, profile.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Please verify your email address"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage()
                                , Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
    private void signIn(){
        Intent intent = mGoogle.getSignInIntent();
        startActivityForResult(intent, 9001);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        Validate validate = new Validate();
        String email = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        if(i==R.id.btnDangNhap){
            if(validate.validateEmail(email,textInputEmail) && validate.validatePassword(password, textInputPassword)
                    ){
                DangNhap();
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
        else if(i==R.id.btnGoogle){
            signIn();

        }

    }
}
