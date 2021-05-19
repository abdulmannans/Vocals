package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SendOTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);

        EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetOTP = findViewById(R.id.buttonGetOTP);



        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uPhn;
                uPhn = inputMobile.getText().toString().trim();

                if (uPhn.isEmpty()){
                    Toast.makeText(SendOTPActivity.this,"Enter Mobile Number",Toast.LENGTH_SHORT).show();
                }else{
                    if (uPhn.length()!=10){
                        Toast.makeText(SendOTPActivity.this,"Invalid Number",Toast.LENGTH_SHORT).show();

                    }else{
                        verify verify = new verify(getApplicationContext());

                        if (verify.isNetworkConnected()){
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference();

                            data.child("Users").child(uPhn).child("phn").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()){
                                        Toast.makeText(SendOTPActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                    }else {
                                        String firePhn = String.valueOf(task.getResult().getValue());
                                        if (uPhn.equals(firePhn)){
                                            int randomPIN = (int)(Math.random()*9000)+1000;
                                            String OTP = String.valueOf(randomPIN);
                                            Toast.makeText(SendOTPActivity.this, OTP,Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                            intent.putExtra("mobile",uPhn);
                                            intent.putExtra("OTP",OTP);
                                            startActivity(intent);

                                        }else {
                                            Toast.makeText(SendOTPActivity.this,"User Doesn't Exists",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(SendOTPActivity.this, "Network Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

    }
}