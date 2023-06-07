package com.example.yourcall;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {
    EditText Gmail, password, repassword;
    Button signin_btn;
    TextView login_txt;
    String userId;
    ProgressBar ProgressBar;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private DatabaseReference usersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Gmail = findViewById(R.id.Gmail);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signin_btn = findViewById(R.id.signin_btn);
        ProgressBar = findViewById(R.id.ProgressBar);
        login_txt = findViewById(R.id.login_txt);
        firebaseFirestore=FirebaseFirestore.getInstance();

        signin_btn.setOnClickListener(v-> createAccount());
        login_txt.setOnClickListener(v->startActivity(new Intent(Signup.this,Login.class)));
    }

    void createAccount(){
        String email  = Gmail.getText().toString();
        String mainpassword  = password.getText().toString();
        String confirmPassword  = repassword.getText().toString();
        boolean isValidated = validateData(email,mainpassword,confirmPassword);
        if(!isValidated){
            return;
        }
        createAccountInFirebase(email,mainpassword);
    }

    void createAccountInFirebase(String email,String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Signup.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            //creating acc is done
                            Utilty.showToast(Signup.this,"Successfully create account");


                            usersRef = FirebaseDatabase.getInstance().getReference("SignUp_users");
                            String confirmPassword  = repassword.getText().toString();

                            // Create a new user instance
                            user_data user = new user_data(email,password,confirmPassword);

                            // Generate a unique key for the user
                            String userId = usersRef.push().getKey();

                            // Store the user data under the generated key
                            usersRef.child(userId).setValue(user);

/*
                            HashMap<String,Object>hashMap=new HashMap<>();
                            hashMap.put("Gmail",email);
                            hashMap.put("Password",password);
                            hashMap.put("rePassword",repassword);
                            databaseReference.child("Users").child(email)
                                    .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Signup.this, "Done", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                                            Toast.makeText(Signup.this, "Fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });



 */
                          //  Utilty.showToast(Signup.this,"Successfully create account,Check email to verify");
                          //  firebaseAuth.getCurrentUser().sendEmailVerification();
                          //  firebaseAuth.signOut();
                          //  finish();

                        }else{
                            //failure
                            usersRef = FirebaseDatabase.getInstance().getReference("SignUp_Failed_users");
                            String confirmPassword  = repassword.getText().toString();

                            // Create a new user instance
                            user_data user1 = new user_data(email,password,confirmPassword);

                            // Generate a unique key for the user
                            String userId = usersRef.push().getKey();

                            // Store the user data under the generated key
                            usersRef.child(userId).setValue(user1);


                            Utilty.showToast(Signup.this,task.getException().getLocalizedMessage());

                        }
                    }
                }
        );
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            ProgressBar.setVisibility(View.VISIBLE);
            signin_btn.setVisibility(View.GONE);
        }else{
            ProgressBar.setVisibility(View.GONE);
            signin_btn.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email,String mainpassword,String confirmPassword){
        //validate the data that are input by user.
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Gmail.setError("Email is invalid");
            return false;
        }
        if(mainpassword.length()<6){
            password.setError("Password length is invalid");
            return false;
        }
        if(!mainpassword.equals(confirmPassword)){
            repassword.setError("Password not matched");
            return false;
        }
        return true;
    }
}
