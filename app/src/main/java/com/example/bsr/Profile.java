package com.example.bsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    ImageView arrow;

    private TextInputEditText profile_name,profile_email, profile_pass;

    private Button updateBtn;

    private FirebaseFirestore db = Session.db;
    private FirebaseAuth mAuth = Session.mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        updateBtn = findViewById(R.id.profileUpdateBtn);

        profile_email = findViewById(R.id.profileEmail);
        profile_name = findViewById(R.id.profileFullName);
        profile_pass=findViewById(R.id.profilePass);


        setFields();

        arrow = findViewById(R.id.image_back);

        updateBtn.setOnClickListener(view -> {
            updateUser();
        });

        arrow.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Dashboard.class);
            startActivity(intent);

        });
    }

    private void updateUser(){

        String d_username = Session.userData.get("username").toString().trim();
        String d_full_name = profile_name.getText().toString().trim();
        String d_profile_email = profile_email.getText().toString().trim();
        String d_profile_pass = profile_pass.getText().toString().trim();

        if (d_username.isEmpty() || d_full_name.isEmpty() || d_profile_email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();

            return;
        }

        if (!d_profile_pass.isEmpty() && d_profile_pass.length() < 6){
            Toast.makeText(getApplicationContext(), "Password must be bigger than 6", Toast.LENGTH_LONG).show();

            return;
        }


        DocumentReference document = db.getInstance().collection("users").document(d_username);

        document.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                HashMap<String, String> user = new HashMap<String, String>();

                user.put("full_name",d_full_name );
                user.put("email", d_profile_email);
                user.put("username", d_username);

                document.set(user);

                Log.d("Taggg",d_profile_email);
                Log.d("Taggg",d_profile_pass);

                Objects.requireNonNull(mAuth.getInstance().getCurrentUser()).updateEmail(d_profile_email);

                if (d_profile_pass.length() > 6){
                    Objects.requireNonNull(mAuth.getInstance().getCurrentUser().updatePassword(d_profile_pass));
                }


                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                finish();
            }
        });

    }


    private void setFields(){
        profile_name.setText(Session.userData.get("full_name").toString());
        profile_email.setText(Session.userData.get("email").toString());
        profile_pass.setText("");
    }
}