package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText pass , crmPass;
    Button changePass;
    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        pass = findViewById(R.id.pass);
        crmPass = findViewById(R.id.cnfrmpass);
        changePass = findViewById(R.id.changePassword);
        String uPhn = getIntent().getStringExtra("mobile");

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uPass = pass.getText().toString().trim();
                String uCPass = crmPass.getText().toString().trim();

                if (uPass.isEmpty() || uCPass.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Enter Password",Toast.LENGTH_SHORT).show();
                }else {
                    if (uPass.length()<8 || uCPass.length()<8){
                        Toast.makeText(ChangePasswordActivity.this, "Enter Min 8 Char Password",Toast.LENGTH_SHORT).show();
                    }else {
                        if (uPass.equals(uCPass)){
                            String hashedPass = hashPass.hashedPass(uPass);
                            DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

                            mData.child("Users").child(uPhn).child("password").setValue(hashedPass);

                            Toast.makeText(ChangePasswordActivity.this, "Password Changed",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangePasswordActivity.this,Login.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();

                        }else {
                            Toast.makeText(ChangePasswordActivity.this, "Password Doesn't Match",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
