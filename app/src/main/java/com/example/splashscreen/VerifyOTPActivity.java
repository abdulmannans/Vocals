package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyOTPActivity extends AppCompatActivity {
    private EditText inputCode1, inputCode2, inputCode3, inputCode4;
    Button buttonVerify;
    TextView Resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        String uPhn = getIntent().getStringExtra("mobile");
        String OTP = getIntent().getStringExtra("OTP");

        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);

        setupOTPInputs();


        String uOTP;


        buttonVerify = findViewById(R.id.buttonVerify);
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent ChangePasswordIntent = new Intent(VerifyOTPActivity.this, ChangePasswordActivity.class);
                    ChangePasswordIntent.putExtra("mobile",uPhn);
                    startActivity( ChangePasswordIntent);


            }
        });



    }
    private void setupOTPInputs(){

        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().trim().isEmpty()){
                inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }
}