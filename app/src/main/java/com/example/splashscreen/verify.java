package com.example.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class verify {
    Context context;

    verify(){

    }
    verify(Context context){
        this.context = context;
    }

    public boolean isNetworkConnected(){

        ConnectivityManager Connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Connection.getActiveNetworkInfo() != null && Connection.getActiveNetworkInfo().isConnected();
    }
}
