package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    TextView profileName, Logout;
    ImageView Back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        profileName = findViewById(R.id.profile);
        Logout = findViewById(R.id.logout);

        SharedPreferences session = getApplicationContext().getSharedPreferences("loggedIN",getApplicationContext().MODE_PRIVATE);
        String uName = session.getString("uName", "");

        profileName.setText(uName);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("loggedIN", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();
                startActivity(new Intent(MenuActivity.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });

        profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, bookNow.class));
            }
        });

    }
}