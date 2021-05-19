package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class userAddress extends AppCompatActivity {

    TextView skip;
    TextView flat, area, land, town, state, country, pin;
    Button getLocation, saveAddress;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);

        if (ActivityCompat.checkSelfPermission(userAddress.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("hello 2 ");
        } else {
            ActivityCompat.requestPermissions(userAddress.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        skip = findViewById(R.id.skip);
        flat = findViewById(R.id.Flat);
        area = findViewById(R.id.Area);
        land = findViewById(R.id.Landmark);
        town = findViewById(R.id.Town);
        state = findViewById(R.id.State);
        country = findViewById(R.id.Country);
        pin = findViewById(R.id.Pin);
        getLocation = findViewById(R.id.getGPSLocation);
        saveAddress = findViewById(R.id.SaveAddress);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userAddress.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uFlat, uArea, uLand, uTown, uState, uCountry, uPin;

                uFlat = flat.getText().toString().trim();
                uArea = area.getText().toString().trim();
                uLand = land.getText().toString().trim();
                uTown = town.getText().toString().trim();
                uState = state.getText().toString().trim();
                uCountry = country.getText().toString().trim();
                uPin = pin.getText().toString().trim();


                if (uFlat.isEmpty()){
                    Toast.makeText(userAddress.this, "Enter Your "+getString(R.string.flat_house_no_building_company_apartment) ,Toast.LENGTH_SHORT).show();
                }else {
                    if (uArea.isEmpty()){
                        Toast.makeText(userAddress.this, "Enter Your "+getString(R.string.area_colony_street_sector_village),Toast.LENGTH_SHORT).show();
                    }else{
                        if (uLand.isEmpty()){
                            Toast.makeText(userAddress.this, "Enter Your "+getString(R.string.landmark_e_g_near_bilal_hospital),Toast.LENGTH_SHORT).show();
                        }else {
                            if (uTown.isEmpty()){
                                Toast.makeText(userAddress.this, "Enter Your "+getString(R.string.town_city),Toast.LENGTH_SHORT).show();
                            }else {
                                if (uState.isEmpty()){
                                    Toast.makeText(userAddress.this, "Enter Your "+getString(R.string.state),Toast.LENGTH_SHORT).show();
                                }else{
                                    if (uCountry.isEmpty()){
                                        Toast.makeText(userAddress.this, "Enter Your "+getString(R.string.country),Toast.LENGTH_SHORT).show();
                                    }else {
                                        if (uPin.isEmpty()){
                                            Toast.makeText(userAddress.this, "Enter Your "+getString(R.string.pin_code),Toast.LENGTH_SHORT).show();
                                        }else {
                                            verify verify = new  verify(getApplicationContext());
                                            if (verify.isNetworkConnected()){
                                                SharedPreferences Username = getApplicationContext().getSharedPreferences("loggedIN", MODE_PRIVATE);
                                                String Phone = Username.getString("phn", "");
                                                DatabaseReference AdData ;
                                                AdData = FirebaseDatabase.getInstance().getReference();
                                                String completeAddress = uFlat+", "+uArea+", "+uLand+", "+uTown+", "+uState+", "+uPin+", "+uCountry;


                                                Address address = new Address(uFlat,uArea,uLand,uTown,uState,uCountry,uPin,completeAddress);
                                                Map<String, Object> userUpdates = address.toMap();
                                                AdData.child("Users").child(Phone).updateChildren(userUpdates);

                                                SharedPreferences userDetails = getSharedPreferences("loggedIN", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = userDetails.edit();
                                                editor.putString("completeAddress", completeAddress);
                                                editor.apply();

                                                Toast.makeText(userAddress.this, "Address Saved.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(userAddress.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                finish();
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

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(userAddress.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();

                            if (location !=null){
                                try {
                                    Geocoder geocoder = new Geocoder(userAddress.this, Locale.getDefault());
                                    List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                                    String gpsArea, gpsLand, gpsTown, gpsState, gpsCountry, gpsPin;

                                    gpsArea = addresses.get(0).getPremises();
                                    gpsLand = addresses.get(0).getThoroughfare();
                                    gpsTown = addresses.get(0).getSubLocality() + " "+addresses.get(0).getLocality();
                                    gpsState = addresses.get(0).getAdminArea();
                                    gpsCountry = addresses.get(0).getCountryName();
                                    gpsPin = addresses.get(0).getPostalCode();

                                    area.setText(gpsArea);
                                    land.setText(gpsLand);
                                    town.setText(gpsTown);
                                    state.setText(gpsState);
                                    country.setText(gpsCountry);
                                    pin.setText(gpsPin);



                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }


                        }
                    });


                } else {
                    ActivityCompat.requestPermissions(userAddress.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

            }
        });
    }

    @IgnoreExtraProperties
    public static class Address{
        public String Flat;
        public String Area;
        public String Landmark;
        public String Town;
        public String State;
        public String Country;
        public String Pin;
        public String completeAddress;

        public Address(){

        }
        public Address(String Flat, String Area, String Landmark, String Town, String State, String Country, String Pin, String completeAddress){
            this.Flat = Flat;
            this.Area = Area;
            this.Landmark = Landmark;
            this.Town = Town;
            this.State = State;
            this.Country = Country;
            this.Pin = Pin;
            this.completeAddress = completeAddress;
        }

        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("Flat", Flat);
            result.put("Area", Area);
            result.put("Landmark", Landmark);
            result.put("Town", Town);
            result.put("State", State);
            result.put("Country", Country);
            result.put("Pin", Pin);
            result.put("completeAddress", completeAddress);

            return result;
        }
    }
}