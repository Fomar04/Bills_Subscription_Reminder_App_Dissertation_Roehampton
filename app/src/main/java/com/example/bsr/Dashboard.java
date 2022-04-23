package com.example.bsr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    CardView cardLogOut;
    CardView cardChart;
    CardView cardBills;
    CardView cardSubscriptions;
    CardView cardProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        cardLogOut = findViewById(R.id.cardLogOut);
        cardChart = findViewById(R.id.cardChart);
        cardBills = findViewById(R.id.cardBills);
        cardSubscriptions = findViewById(R.id.cardSubscriptions);
        cardProfile = findViewById(R.id.cardProfile);


        cardBills.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, Bills.class);
            startActivity(intent);

        });
// I need to make a class for log out
        cardLogOut.setOnClickListener((View view) -> {
            FirebaseAuth.getInstance().signOut();

            //When we exit we don't want data to persist
            Session.authenticatedUser = null;
            Session.userData = null;
           finish();
        });

        cardChart.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, Chart.class);
            startActivity(intent);
        });

        cardSubscriptions.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, Subscriptions.class);
            startActivity(intent);

        });

        cardProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, Profile.class);
            startActivity(intent);

        });


    }


}