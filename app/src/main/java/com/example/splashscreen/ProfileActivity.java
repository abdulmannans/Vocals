package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    TextView fullName, Phone, Email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.fullName);
        Phone = findViewById(R.id.phoneNo);
        Email = findViewById(R.id.email);

        SharedPreferences session = getApplicationContext().getSharedPreferences("loggedIN",getApplicationContext().MODE_PRIVATE);
        String uName , uPhn, uEmail, uAddress;

        uName = session.getString("uName", "");
        fullName.setText(uName);
        uPhn = session.getString("phn", "");
        Phone.setText(uPhn);
        uEmail = session.getString("email", "");
        Email.setText(uEmail);

    }
}