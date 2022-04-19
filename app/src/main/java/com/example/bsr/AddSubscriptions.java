package com.example.bsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class AddSubscriptions extends AppCompatActivity {

    ImageView arrow;

    private FirebaseFirestore db = Session.db;

    TextInputEditText description, date, amount;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_subscriptions);

        description = findViewById(R.id.addSDescription);
        date = findViewById(R.id.addSDate);
        amount = findViewById(R.id.addSAmount);
        submit = findViewById(R.id.addSSubmit);

        submit.setOnClickListener(view -> {
            String s_description = description.getText().toString().trim();

            String s_date = date.getText().toString().trim();

            String s_amount = amount.getText().toString().trim();

            addSubscription(s_description, s_date, s_amount);
        });

        arrow = findViewById(R.id.image_back);

        arrow.setOnClickListener(view -> {
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);

        });
    }

    public void addSubscription(String description, String date, String amount) {

        String uniqueID = UUID.randomUUID().toString();

        DocumentReference docIdRef = db.getInstance().collection("subscriptions").document(uniqueID);

        HashMap<String, String> subscription = new HashMap<String, String>();
        subscription.put("user_name", Session.userData.get("username").toString());
        subscription.put("description", description);
        subscription.put("date", date.toString());
        subscription.put("amount", amount);

        docIdRef.get().addOnCompleteListener(firestore_task -> {
            if (firestore_task.isSuccessful()) {
                DocumentSnapshot document = firestore_task.getResult();
                docIdRef.set(subscription);
                IBinder token = findViewById(android.R.id.content).getWindowToken();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(token, 0);

                Toast.makeText(getApplicationContext(), "Subscription created!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
            }

        });

    }

}