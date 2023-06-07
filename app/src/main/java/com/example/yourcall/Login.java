package com.example.yourcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.checkerframework.checker.nullness.qual.NonNull;

public class Login extends AppCompatActivity {

    EditText gmail1,password;
    Button login_btn;
    TextView signup_txt;
    ProgressBar progressBar;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gmail1=findViewById(R.id.gmail1);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.ProgressBar);
        signup_txt=findViewById(R.id.signup_txt);

        signup_txt.setOnClickListener((v)->startActivity(new Intent(Login.this,Signup.class)));

        login_btn.setOnClickListener((v)-> loginUser() );
    }
    void loginUser(){
        String email  = gmail1.getText().toString();
        String mainpassword  = password.getText().toString();


        boolean isValidated = validateData(email,mainpassword);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,mainpassword);

    }

    void loginAccountInFirebase(String email,String mainpassword){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,mainpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //login is success

                   // if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to mainactivity
                    Toast.makeText(Login.this, "Login Succesfull दोस्त", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                    //}else{
                   //     Utilty.showToast(Login.this,"Email not verified, Please verify your email.");
                   // }

                }else{
                    //login failed

                    Utilty.showToast(Login.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            login_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String mainpassword){

        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            gmail1.setError("Email is invalid");
            return false;
        }
        if(mainpassword.length()<6){
            password.setError("Password length is invalid");
            return false;
        }
        return true;
    }

}