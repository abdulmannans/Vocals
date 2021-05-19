package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class HomePage extends AppCompatActivity {

    ImageView imageView;
    LinearLayout linearLayout;
    TextView loc;
    public static final Map<Integer, Integer> ITEM_MAP = new HashMap<Integer, Integer>();
    hashPass hashPass;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        loc = findViewById(R.id.your_location);
        SharedPreferences session = getApplicationContext().getSharedPreferences("loggedIN", MODE_PRIVATE);
        String uAddress = session.getString("completeAddress", "");

        hashPass = new hashPass();

        if (uAddress.equals("") && hashPass.Count() == 1 ){
            startActivity(new Intent(HomePage.this, userAddress.class));
            hashPass.Count();
        } else {
            loc.setText(uAddress);
        }


        imageView = findViewById(R.id.imageMenu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainMenuIntent = new Intent(HomePage.this, MenuActivity.class);
                startActivity(MainMenuIntent);

            }
        });
        FirebaseDatabase sData = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = sData.getReference("Services/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Object>> objectGTI = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                Map<String, Object> objectMap = snapshot.getValue(objectGTI);
                ArrayList<Object> objectArrayList = new ArrayList<Object>(objectMap.values());
                linearLayout = findViewById(R.id.linearLayout);
                linearLayout.removeAllViews();

                if (objectArrayList.size()>0){
                    int i = 0;

                    for (Object value : objectArrayList) {

                        String Service = value.toString();
                        int id = ViewCompat.generateViewId();


                        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                        relativeLayout.setId(id);
                        ITEM_MAP.put(i,id);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                        params.height = 300;
                        params.rightMargin = 10;
                        params.topMargin = 10;
                        params.bottomMargin = 10;
                        params.leftMargin = 10;
                        relativeLayout.setBackgroundResource(R.drawable.location_bar_button);
                        relativeLayout.setLayoutParams(params);
                        linearLayout.addView(relativeLayout);
                        TableRow.LayoutParams txtParams = new TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                        TextView textView = new TextView(getApplicationContext());
                        textView.setId(R.id.textview);
                        textView.setPadding(70, 90, 100, 100);
                        textView.setTextColor(BLACK);
                        textView.setTextSize(30);
                        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/robotoslab.ttf");
                        textView.setTypeface(face, Typeface.BOLD);
                        textView.setText(Service);
                        textView.setLayoutParams(txtParams);
                        relativeLayout.addView(textView);
                        ImageView imageView = new ImageView(getApplicationContext());
                        relativeLayout.addView(imageView);
                        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                        imgParams.height = 250;
                        imgParams.width = 250;
                        imgParams.topMargin = 45;
                        imgParams.rightMargin = 35;
                        imgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        imgParams.addRule(RelativeLayout.LEFT_OF, R.id.textview);
                        imageView.setBackgroundResource(R.drawable.carpenter);
                        imageView.setLayoutParams(imgParams);

                        int layout = ITEM_MAP.get(i);
                        RelativeLayout Real =findViewById(layout);

                        Real.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HomePage.this, Services.class).putExtra("Service",Service));
                            }
                        });
                        i++;




                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.co.in/maps?q=" + uAddress;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }
}




