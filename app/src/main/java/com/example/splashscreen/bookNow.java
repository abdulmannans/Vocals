package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class bookNow extends AppCompatActivity {
    CheckBox checkBox;
    EditText dateSelector,timeSelector;
    Button Payment;
    public static ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);


        for (int i =0 ; i<10; i++){
            list.add("Hello"+i);
        }

        checkBox = findViewById(R.id.Address);
        SharedPreferences session = getApplicationContext().getSharedPreferences("loggedIN", MODE_PRIVATE);
        String address = session.getString("completeAddress", "");
        checkBox.setText(address);

        dateSelector = findViewById(R.id.dateSelector);



        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
                materialDateBuilder.setTitleText("SELECT DATE");

                final  MaterialDatePicker materialDatePicker = materialDateBuilder.build();
                materialDatePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateSelector.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });

        timeSelector = findViewById(R.id.timeSelector);
        timeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(bookNow.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeSelector.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });




        Payment = findViewById(R.id.Payment);

        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    String Address = String.valueOf(checkBox.getText()).trim();
                    String Time = String.valueOf(timeSelector.getText()).trim();
                    String Date = String.valueOf(dateSelector.getText()).trim();
                    String orderId = getIntent().getStringExtra("orderID");
                    String Total = getIntent().getStringExtra("total");
                    String Charges = getIntent().getStringExtra("Charges");
                    if (!Date.isEmpty()){
                        if (!Time.isEmpty()){
                            ADT adt = new ADT(Address, Date, Time);
                            Map<String, Object> list = adt.toMap();

                            verify verify = new verify(getApplicationContext());
                            if (verify.isNetworkConnected()){

                                DatabaseReference update;

                                update = FirebaseDatabase.getInstance().getReference();

                                update.child("Orders").child(orderId).updateChildren(list);

                                Intent intent = new Intent(bookNow.this, paymentGateway.class);
                                intent.putExtra("orderID", orderId);
                                intent.putExtra("total", Total);
                                intent.putExtra("Charges", Charges);
                                startActivity(intent);

                            }



                        }else {
                            Toast.makeText(getApplicationContext(),"Please Select Time.",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please Select Date.",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please Select Address",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @IgnoreExtraProperties
    public static class ADT{
        public String Address;
        public String Date;
        public String Time;

        ADT(){

        }
        ADT(String Address, String Date, String Time){
            this.Address = Address;
            this.Date = Date;
            this.Time = Time;

        }
        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("Address", Address);
            result.put("Date", Date);
            result.put("Time", Time);

            return result;
        }
    }
}