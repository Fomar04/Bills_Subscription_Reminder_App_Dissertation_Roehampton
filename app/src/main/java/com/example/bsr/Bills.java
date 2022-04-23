package com.example.bsr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class Bills extends AppCompatActivity {

    FloatingActionButton add_btn;
    ImageView arrow;
    private FirebaseFirestore db = Session.db;
    private RecyclerView recyclerView;
    private ArrayList<Billable> bills_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bills);

        bills_list = new ArrayList<>();

        recyclerView = findViewById(R.id.rvBills);

        add_btn = findViewById(R.id.add_btn);

        add_btn.setOnClickListener(view -> {
            Intent intent = new Intent(Bills.this, AddBills.class);
            startActivity(intent);
            //We repopulate with the fresh data
            retrieveBills();
        });

        arrow = findViewById(R.id.image_back);

        arrow.setOnClickListener(view -> {
            Intent intent = new Intent(Bills.this, Dashboard.class);
            startActivity(intent);
        });

        retrieveBills();
    }


    private void retrieveBills() {

        db.getInstance().collection("bills").whereEqualTo("user_name", Session.userData.get("username")).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot result : Objects.requireNonNull(task.getResult())
                ) {

                    String description = result.get("description").toString();
                    String amount = result.get("amount").toString();
                    String date = result.get("date").toString();
                    String id = result.getId();


                    bills_list.add(new Billable(description, date, amount));
                    setAdapter();
                }
            }
        });
    }

    private void setAdapter() {
        billableAdapter adapter = new billableAdapter(new ArrayList(bills_list), "bills", this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}