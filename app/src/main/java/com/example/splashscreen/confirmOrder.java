package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class confirmOrder extends AppCompatActivity {
    LinearLayout linearLayout;
    public static int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        String orderId = getIntent().getStringExtra("orderID");
        String Total = getIntent().getStringExtra("total");
        String Charges = getIntent().getStringExtra("Charges");

        linearLayout = findViewById(R.id.linearLayout);

        linearLayout.removeAllViews();

        FirebaseDatabase Address = FirebaseDatabase.getInstance();
        DatabaseReference addressReference = Address.getReference("Orders/"+orderId+"/Address");
        addressReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RelativeLayout relativeLayout1 = new RelativeLayout(getApplicationContext());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                    params.height = 300;
                    params.rightMargin = 10;
                    params.topMargin = 10;
                    params.bottomMargin = 10;
                    params.leftMargin = 10;
                    relativeLayout1.setBackgroundResource(R.drawable.location_bar_button);
                    relativeLayout1.setLayoutParams(params);
                    linearLayout.addView(relativeLayout1);
                    TextView textView = new TextView(getApplicationContext());
                    textView.setTextColor(BLACK);
                    textView.setTextSize(20);
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/robotoslab.ttf");
                    textView.setTypeface(face, Typeface.BOLD);
                    textView.setText(String.valueOf(dataSnapshot));
                    relativeLayout1.addView(textView);
                    RelativeLayout.LayoutParams txtParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                    txtParams.height = MATCH_PARENT;
                    txtParams.width = WRAP_CONTENT;
                    txtParams.topMargin = i;
                    txtParams.leftMargin = 35;
                    txtParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    txtParams.addRule(RelativeLayout.RIGHT_OF, R.id.text);
                    textView.setLayoutParams(txtParams);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        FirebaseDatabase Data = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = Data.getReference("Orders/"+orderId+"/items/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<vendorSubServices.Sub> subList = new ArrayList<>();
                subList.clear();
                i = 45;
                RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                params.rightMargin = 10;
                params.topMargin = 10;
                params.bottomMargin = 10;
                params.leftMargin = 10;
                relativeLayout.setBackgroundResource(R.drawable.location_bar_button);
                relativeLayout.setLayoutParams(params);
                linearLayout.addView(relativeLayout);

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    vendorSubServices.Sub sub = dataSnapshot.getValue(vendorSubServices.Sub.class);
                    subList.add(sub);
                    String name = sub.getName(), price = sub.getPrice();
                    TextView textView = new TextView(getApplicationContext());
                    textView.setTextColor(BLACK);
                    textView.setTextSize(20);
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/robotoslab.ttf");
                    textView.setTypeface(face, Typeface.BOLD);
                    textView.setText(name);
                    relativeLayout.addView(textView);
                    RelativeLayout.LayoutParams txtParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                    txtParams.height = MATCH_PARENT;
                    txtParams.width = WRAP_CONTENT;
                    txtParams.topMargin = i;
                    txtParams.leftMargin = 35;
                    txtParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    txtParams.addRule(RelativeLayout.RIGHT_OF, R.id.text);
                    textView.setLayoutParams(txtParams);
                    TextView text = new TextView(getApplicationContext());
                    text.setTextColor(BLACK);
                    textView.setTypeface(face, Typeface.BOLD);
                    text.setTextSize(20);
                    text.setText("\u20B9"+price);
                    relativeLayout.addView(text);
                    RelativeLayout.LayoutParams ChargeParams = (RelativeLayout.LayoutParams) text.getLayoutParams();
                    ChargeParams.height = MATCH_PARENT;
                    ChargeParams.width = WRAP_CONTENT;
                    ChargeParams.topMargin = i;
                    ChargeParams.rightMargin =35;
                    ChargeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ChargeParams.addRule(RelativeLayout.LEFT_OF, R.id.textview);
                    text.setLayoutParams(ChargeParams);
                    i+=45;




                }

                i+=45;
                TextView textView = new TextView(getApplicationContext());
                textView.setTextColor(BLACK);
                textView.setTextSize(20);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/robotoslab.ttf");
                textView.setTypeface(face, Typeface.BOLD);
                textView.setText("Visiting Charges");
                relativeLayout.addView(textView);
                RelativeLayout.LayoutParams txtParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                txtParams.height = MATCH_PARENT;
                txtParams.width = WRAP_CONTENT;
                txtParams.topMargin = i;
                txtParams.leftMargin = 35;
                txtParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                txtParams.addRule(RelativeLayout.RIGHT_OF, R.id.text);
                textView.setLayoutParams(txtParams);
                TextView text = new TextView(getApplicationContext());
                text.setTextColor(BLACK);
                textView.setTypeface(face, Typeface.BOLD);
                text.setTextSize(20);
                text.setText("\u20B9"+Charges);
                relativeLayout.addView(text);
                RelativeLayout.LayoutParams ChargeParams = (RelativeLayout.LayoutParams) text.getLayoutParams();
                ChargeParams.height = MATCH_PARENT;
                ChargeParams.width = WRAP_CONTENT;
                ChargeParams.topMargin = i;
                ChargeParams.rightMargin =35;
                ChargeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ChargeParams.addRule(RelativeLayout.LEFT_OF, R.id.textview);
                text.setLayoutParams(ChargeParams);

                i+=45;
                TextView text1 = new TextView(getApplicationContext());
                text1.setTextColor(BLACK);
                text1.setTextSize(20);
                text1.setTypeface(face, Typeface.BOLD);
                text1.setText("Total");
                relativeLayout.addView(text1);
                RelativeLayout.LayoutParams txtParam = (RelativeLayout.LayoutParams) text1.getLayoutParams();
                txtParam.height = MATCH_PARENT;
                txtParam.width = WRAP_CONTENT;
                txtParam.topMargin = i;
                txtParam.leftMargin = 35;
                txtParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                txtParam.addRule(RelativeLayout.RIGHT_OF, R.id.text);
                text1.setLayoutParams(txtParam);
                TextView text2 = new TextView(getApplicationContext());
                text2.setTextColor(BLACK);
                text2.setTypeface(face, Typeface.BOLD);
                text2.setTextSize(20);
                text2.setText("\u20B9"+Total);
                relativeLayout.addView(text2);
                RelativeLayout.LayoutParams ChargeParam = (RelativeLayout.LayoutParams) text2.getLayoutParams();
                ChargeParam.height = MATCH_PARENT;
                ChargeParam.width = WRAP_CONTENT;
                ChargeParam.topMargin = i;
                ChargeParam.rightMargin =35;
                ChargeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ChargeParam.addRule(RelativeLayout.LEFT_OF, R.id.textview);
                text2.setLayoutParams(ChargeParam);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }
    @IgnoreExtraProperties
    static class Sub {

        String name;

        String price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}