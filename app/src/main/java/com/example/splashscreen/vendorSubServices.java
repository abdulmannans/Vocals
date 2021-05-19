package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.awt.font.TextAttribute;
import java.sql.Timestamp;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static java.awt.font.TextAttribute.SUPERSCRIPT_SUPER;

public class vendorSubServices extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView vendorName, vendorCharges;
    public static final Map<Integer, Integer> ITEM_MAP = new HashMap<Integer, Integer>();
    public static final Map<Integer, String> NAME_MAP = new HashMap<Integer, String>();
    public static final Map<Integer, String> PRICE_MAP = new HashMap<Integer, String>();
    public static final ArrayList<String> Bill =new ArrayList<String>();
    public static Integer Total = 0;
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

    Button booKNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_subservices);

        String Name = getIntent().getStringExtra("Name");
        String Charges = getIntent().getStringExtra("Charges");
        String Phone = getIntent().getStringExtra("phn");
        linearLayout = findViewById(R.id.linearLayout);

        vendorName = findViewById(R.id.vendorName);
        vendorCharges = findViewById(R.id.minCharges);

        vendorName.setText(Name);
        vendorCharges.setText("\u20B9"+Charges);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Vendors/" + Phone + "/sub/");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Sub> subList = new ArrayList<>();
                subList.clear();
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int LaId = ViewCompat.generateViewId();
                    Sub sub = dataSnapshot.getValue(Sub.class);
                    subList.add(sub);

                    String name = sub.getName(), price = sub.getPrice();

                    RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                    relativeLayout.setId(i);
                    params.height = 170;
                    params.rightMargin = 10;
                    params.topMargin = 10;
                    params.bottomMargin = 10;
                    params.leftMargin = 10;
                    relativeLayout.setBackgroundResource(R.drawable.location_bar_button);
                    relativeLayout.setLayoutParams(params);
                    linearLayout.addView(relativeLayout);
                    TableRow.LayoutParams txtParams = new TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                    CheckBox textView = new CheckBox(getApplicationContext());
                    textView.setId(LaId);
                    ITEM_MAP.put(i, LaId);
                    textView.setPadding(35, 45, 50, 50);
                    textView.setTextColor(BLACK);
                    textView.setTextSize(20);
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/robotoslab.ttf");
                    textView.setTypeface(face, Typeface.BOLD);
                    textView.setText(name);
                    textView.setLayoutParams(txtParams);
                    relativeLayout.addView(textView);
                    TextView text = new TextView(getApplicationContext());
                    text.setTextColor(BLACK);
                    textView.setTypeface(face, Typeface.BOLD);
                    text.setTextSize(20);
                    text.setText("\u20B9"+price);
                    relativeLayout.addView(text);
                    RelativeLayout.LayoutParams ChargeParams = (RelativeLayout.LayoutParams) text.getLayoutParams();
                    ChargeParams.height = MATCH_PARENT;
                    ChargeParams.width = WRAP_CONTENT;
                    ChargeParams.topMargin = 45;
                    ChargeParams.rightMargin =35;
                    ChargeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ChargeParams.addRule(RelativeLayout.LEFT_OF, R.id.textview);
                    text.setLayoutParams(ChargeParams);
                    NAME_MAP.put(i,name);
                    PRICE_MAP.put(i,price);
                    i++;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        booKNow = findViewById(R.id.bookNow);
        booKNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String orderId = sdf1.format(timestamp);

                verify verify = new verify(getApplicationContext());


                if (verify.isNetworkConnected()){
                    DatabaseReference Data;
                    Data = FirebaseDatabase.getInstance().getReference();

                    Order order = new Order(Phone,Phone);
                    Data.child("Orders").child(orderId).setValue(order);
                    linearLayout = findViewById(R.id.linearLayout);
                    ArrayList<String> productList = new ArrayList<String>();
                    ArrayList<String> priceList = new ArrayList<String>();

                    productList.add("Minimum Charges");
                    priceList.add(Charges);

                    for (int i =0; i<linearLayout.getChildCount(); i++){
                        CheckBox textView = findViewById(ITEM_MAP.get(i));
                        if (textView.isChecked()){
                            Items items = new Items(NAME_MAP.get(i), PRICE_MAP.get(i));

                            Total += Integer.parseInt(PRICE_MAP.get(i));
                            DatabaseReference product;
                            product = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> list = items.toMap();

                            product.child("Orders").child(orderId).child("items").child(String.valueOf(i)).updateChildren(list);
                            }
                        }
                    Total += Integer.parseInt(Charges);
                    Bill bill = new Bill(Total);
                    Map<String, Object> objectMap = bill.billMap();
                    DatabaseReference totalBill;
                    totalBill = FirebaseDatabase.getInstance().getReference();
                    totalBill.child("Orders").child(orderId).updateChildren(objectMap);

                    minCharge minCharge = new minCharge(Integer.parseInt(Charges));
                    Map<String, Object> map = minCharge.chargeMap();
                    DatabaseReference min;
                    min = FirebaseDatabase.getInstance().getReference();
                    min.child("Orders").child(orderId).updateChildren(map);

                }
                Intent intent = new Intent(vendorSubServices.this, bookNow.class);
                intent.putExtra("orderID", orderId);
                intent.putExtra("total", String.valueOf(Total));
                intent.putExtra("Charges", Charges);
                startActivity(intent);




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
    @IgnoreExtraProperties
    public static class Order{
        public String UserID;
        public String VendorID;

        Order(){

        }
        Order(String UserID, String VendorID){
            this.UserID = UserID;
            this.VendorID = VendorID;
        }

    }
    @IgnoreExtraProperties
    public static class Items{
        public String Name;
        public String Price;

        Items(){

        }
        Items(String Name, String Price){
            this.Name = Name;
            this.Price = Price;
        }
        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("name", Name);
            result.put("price", Price);

            return result;
        }
    }
    @IgnoreExtraProperties
    public static class Bill{
        public Integer total;

        Bill(){

        }

        Bill(Integer total){
            this.total = total;

        }
        @Exclude
        public Map<String, Object> billMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("Total", total);

            return result;

        }
    }
    @IgnoreExtraProperties
    public static class minCharge{
        public Integer Charges;

        minCharge(){

        }

        minCharge(Integer Charges){
            this.Charges = Charges;

        }
        @Exclude
        public Map<String, Object> chargeMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("MinCharges", Charges);

            return result;

        }
    }


}