package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class paymentGateway extends AppCompatActivity {
    Button placeOrder;
    RadioGroup PaymentType;
    RadioButton Option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        placeOrder = findViewById(R.id.placeOrder);
        PaymentType = findViewById(R.id.PaymentType);

        placeOrder.setOnClickListener(v -> {
            String orderId = getIntent().getStringExtra("orderID");
            String Total = getIntent().getStringExtra("total");
            String Charges = getIntent().getStringExtra("Charges");
            int selectedID = PaymentType.getCheckedRadioButtonId();
            Option = findViewById(selectedID);
            String payment = String.valueOf(Option.getText());

            order order = new order(payment);
            Map<String, Object> objectMap = order.toMap();

            DatabaseReference update;
            update = FirebaseDatabase.getInstance().getReference();

            update.child("Orders").child(orderId).updateChildren(objectMap);

            Intent intent = new Intent(paymentGateway.this, confirmOrder.class);
            intent.putExtra("orderID", orderId);
            intent.putExtra("total", Total);
            intent.putExtra("Charges", Charges);
            startActivity(intent);



        });


    }
    @IgnoreExtraProperties
    public static class order{
        public String payment;

        order(){

        }
        order(String payment){
            this.payment = payment;
        }
        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("Payment",payment);

            return result;

        }
    }
}