package com.example.bsr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class Subscriptions extends AppCompatActivity {

    FloatingActionButton add_btn;
    ImageView arrow;
    private FirebaseFirestore db = Session.db;
    private RecyclerView recyclerView;
    private ArrayList<Billable> subscriptions_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subscriptions);

        recyclerView = findViewById(R.id.rvSubscriptions);

        subscriptions_list = new ArrayList<>();

        add_btn = findViewById(R.id.add_btn);

        add_btn.setOnClickListener(view -> {
            Intent intent = new Intent(Subscriptions.this, AddSubscriptions.class);
            startActivity(intent);

            //After we add a subscription, we want to go to dashboard
            retrieveBooks();
        });

        arrow = findViewById(R.id.image_back);

        arrow.setOnClickListener(view -> {
            Intent intent = new Intent(Subscriptions.this, Dashboard.class);
            startActivity(intent);

        });

        retrieveBooks();
    }

    private void retrieveBooks() {
        
        db.getInstance().collection("subscriptions").whereEqualTo("user_name", Session.userData.get("username")).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot result : Objects.requireNonNull(task.getResult())
                ) {

                    String provider = result.get("provider").toString();
                    String amount = result.get("amount").toString();
                    String date = result.get("date").toString();

                    subscriptions_list.add(new Billable(provider, date, amount));
                    setAdapter();
                                    }
            }
        });
    }

    private void setAdapter() {
        billableAdapter adapter = new billableAdapter(new ArrayList(subscriptions_list),"subscriptions",this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}