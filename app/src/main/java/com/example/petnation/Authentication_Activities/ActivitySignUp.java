package com.example.petnation.Authentication_Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petnation.Models.User;
import com.example.petnation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignUp extends AppCompatActivity {
    EditText editname,editemail,editpassword,editphone;
    Button signup;
    TextView goback;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        editname=findViewById(R.id.editname);
        editemail=findViewById(R.id.editemail);
        editpassword=findViewById(R.id.editpassword);
        editphone=findViewById(R.id.editphone);
        signup=findViewById(R.id.signup_btn);
        goback=findViewById(R.id.back_text);
        mAuth=FirebaseAuth.getInstance();
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySignUp.this,ActivityLogin.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }
    private void registerUser(){
        String name= editname.getText().toString().trim();
        String email= editemail.getText().toString().trim();
        String password = editpassword.getText().toString().trim();
        String phone=editphone.getText().toString().trim();

        if(name.isEmpty()){
            editname.setError("Required Field!");
            editname.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editemail.setError("Required Field!");
            editemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editpassword.setError("Required Field!");
            editpassword.requestFocus();
            return;
        }
        if(phone.isEmpty()){
            editphone.setError("Required Field!");
            editphone.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ActivitySignUp.this, "User registered!", Toast.LENGTH_SHORT).show();
                            User user = new User(name,email,phone);
                            FirebaseDatabase.getInstance().getReference("users")
                            .child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Map<String,Object> m =new HashMap<>();
                                        m.put("name",name);
                                        m.put("phone",phone);
                                        m.put("email",email);
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(mAuth.getCurrentUser().getUid()).child("animals_reported").setValue(m);

                                        Log.v("signup","Added to databse");
                                        startActivity(new Intent(ActivitySignUp.this,ActivityLogin.class));

                                    }else{
                                        Log.v("signup"," failed");
                                    }
                                }
                            });

                        }
                    }
                });
    }
}