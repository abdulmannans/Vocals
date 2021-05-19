package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class  Services extends AppCompatActivity {
    LinearLayout linearLayout;
    public static final Map<String, Integer> ITEM_MAP = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        String Service = getIntent().getStringExtra("Service");

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        DatabaseReference vendorDb = FirebaseDatabase.getInstance().getReference().child("Vendors");

        Query query = vendorDb.orderByChild("Category").equalTo(Service);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, Object> objectMap = (Map<String, Object>) snapshot.getValue();
                String name = (String) objectMap.get("fullName");
                String Charges = (String) objectMap.get("Charges");
                String phn = (String) objectMap.get("phn");


                if (objectMap.size()>0){

                        int id = ViewCompat.generateViewId();

                        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                        relativeLayout.setId(id);
                        ITEM_MAP.put(name,id);
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
                        textView.setText(name);
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

                    int layout = ITEM_MAP.get(name);
                    RelativeLayout Real =findViewById(layout);

                    Real.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Services.this, vendorSubServices.class);
                            intent.putExtra("Name", name);
                            intent.putExtra("Charges", Charges);
                            intent.putExtra("phn", phn);
                            startActivity(intent);

                        }
                    });


                }else{
                    RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
                    params.height = 170;
                    params.rightMargin = 10;
                    params.topMargin = 10;
                    params.bottomMargin = 10;
                    params.leftMargin = 10;
                    relativeLayout.setBackgroundResource(R.drawable.location_bar_button);
                    relativeLayout.setLayoutParams(params);
                    linearLayout.addView(relativeLayout);
                    TableRow.LayoutParams txtParams= new TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                    TextView textView = new TextView(getApplicationContext());
                    textView.setId(R.id.text);
                    textView.setPadding(35,45,50,50);
                    textView.setTextColor(BLACK);
                    textView.setTextSize(20);
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/roboto.ttf");
                    textView.setTypeface(face, Typeface.BOLD);
                    textView.setText("No Records Found.");
                    textView.setLayoutParams(txtParams);
                    relativeLayout.addView(textView);

                }
                }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
