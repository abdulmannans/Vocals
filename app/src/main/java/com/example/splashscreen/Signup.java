package com.example.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.math.BigInteger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;

public class Signup extends AppCompatActivity {

    Button login;
    Button signUp;
    EditText fullName, email, phn, password;
    private DatabaseReference mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login = findViewById(R.id.Alogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpIntent = new Intent(Signup.this, Login.class);
                startActivity(SignUpIntent);
                finish();
            }
        });
        signUp = findViewById(R.id.signUp);

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.userEmail);
        phn = findViewById(R.id.userPhn);
        password = findViewById(R.id.userPass);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName, uEmail, uPhn, uAddress, uPassword;

                uName = fullName.getText().toString().trim();
                uEmail = email.getText().toString().trim();
                uPhn = phn.getText().toString().trim();
                uPassword = password.getText().toString().trim();
                String hashedPass;

                hashedPass = hashPass.hashedPass(uPassword);

                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(uEmail);

                if (uName.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Your Name.", Toast.LENGTH_SHORT).show();
                } else {
                    if (uEmail.isEmpty()) {
                        Toast.makeText(Signup.this, "Enter Your Email ID.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!matcher.matches()) {
                            Toast.makeText(Signup.this, "Enter Valid Email ID.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (uPhn.isEmpty()) {
                                Toast.makeText(Signup.this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                            } else {
                                if (uPhn.length() < 10) {
                                    Toast.makeText(Signup.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (uPhn.length() > 10) {
                                        Toast.makeText(Signup.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (uPassword.isEmpty()) {
                                            Toast.makeText(Signup.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (uPassword.length()<8){
                                                Toast.makeText(Signup.this,"Enter Minimum 8 Digit Password", Toast.LENGTH_SHORT).show();
                                            }else {
                                                verify verify = new verify(getApplicationContext());
                                                if (verify.isNetworkConnected()){
                                                    DatabaseReference Data;
                                                    Data = FirebaseDatabase.getInstance().getReference();

                                                    Data.child("Users").child(uPhn).child("phn").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                            String firePhn = String.valueOf(task.getResult().getValue()).trim();
                                                            if (!task.isSuccessful()){
                                                                Toast.makeText(Signup.this, "Something Went Wrong",Toast.LENGTH_SHORT).show();
                                                            }else {
                                                                if (uPhn.equals(firePhn)){
                                                                    Toast.makeText(Signup.this, "User Already Exist.",Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    DatabaseReference uData;
                                                                    uData = FirebaseDatabase.getInstance().getReference();
                                                                    User user = new User(uName,uEmail,uPhn,hashedPass);
                                                                    uData.child("Users").child(uPhn).setValue(user);
                                                                    Toast.makeText(Signup.this, "Please Login", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(Signup.this, Login.class));
                                                                }
                                                            }

                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(Signup.this, "Network Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }


            }
        });
    }

    @IgnoreExtraProperties
    public static class User {
        public String fullName;
        public String email;
        public String phn;
        public String password;

        public User() {

        }

        public User(String fullName, String email, String phn,String password) {
            this.fullName = fullName;
            this.email = email;
            this.phn = phn;
            this.password = password;
        }
    }
}
