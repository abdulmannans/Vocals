package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;


public class Login extends AppCompatActivity {

    Button forgetPassword, login;
    MaterialButton newUser;
    EditText phn, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        SharedPreferences Username = getApplicationContext().getSharedPreferences("loggedIN", MODE_PRIVATE);
        String session = Username.getString("uName", "");
        if (!session.equals("")){
            startActivity(new Intent(Login.this, HomePage.class));
            finish();
        }else{
            System.out.println("hello");
        }


        newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpIntent = new Intent(Login.this , Signup.class);
                startActivity(SignUpIntent);
            }
        });

        forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ForgetPasswordIntent = new Intent(Login.this, SendOTPActivity.class);
                startActivity(ForgetPasswordIntent);

            }
        });

        phn = findViewById(R.id.uPhn);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uPhn = phn.getText().toString().trim();
                String pass = password.getText().toString().trim();

                String hashedPass = hashPass.hashedPass(pass);

                if (uPhn.isEmpty()){
                    Toast.makeText(Login.this, "Enter Your Phone Number.",Toast.LENGTH_SHORT).show();
                }else{
                    if (uPhn.length() != 10){
                        Toast.makeText(Login.this, "Invalid Phone Number",Toast.LENGTH_SHORT).show();
                    }else {
                        if (pass.isEmpty()){
                            Toast.makeText(Login.this, "Enter Your Password.",Toast.LENGTH_SHORT).show();
                        }else{
                            verify verify = new verify(getApplicationContext());
                            if (verify.isNetworkConnected()){
                                FirebaseDatabase mFData = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = mFData.getReference("Users/"+uPhn);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Map<String, Object> objectMap = (Map<String, Object>) snapshot.getValue();
                                        String name = (String) objectMap.get("fullName");
                                        String password = (String) objectMap.get("password");
                                        String address = (String) objectMap.get("completeAddress");
                                        String Phone = (String) objectMap.get("phn");
                                        String Email = (String) objectMap.get("email");


                                        if (hashedPass.equals(password)){
                                            SharedPreferences userDetails = getSharedPreferences("loggedIN", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = userDetails.edit();
                                            editor.putString("uName", name);
                                            editor.putString("completeAddress", address);
                                            editor.putString("phn", Phone);
                                            editor.putString("email", Email);

                                            editor.apply();
                                            startActivity(new Intent(Login.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            finish();
                                        }else{
                                            Toast.makeText(Login.this,"Wrong Username Or Password",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else{
                                Toast.makeText(Login.this, "Network Error",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }

            }
        });
    }
}