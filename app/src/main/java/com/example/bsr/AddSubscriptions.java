package com.example.bsr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class AddSubscriptions extends AppCompatActivity {

    ImageView arrow;

    private FirebaseFirestore db = Session.db;

    TextInputEditText description, amount;

    Button submit;

    TextView billDate;

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_subscriptions);

        description = findViewById(R.id.addSDescription);
        billDate = findViewById(R.id.addSDate);
        amount = findViewById(R.id.addSAmount);
        submit = findViewById(R.id.addSSubmit);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        billDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddSubscriptions.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        billDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(view -> {
            String s_description = description.getText().toString().trim();

            String s_date = billDate.getText().toString().trim();

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