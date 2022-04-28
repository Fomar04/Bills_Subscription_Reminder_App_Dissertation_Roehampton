package com.example.bsr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    //Variables
    Button go;
    TextInputEditText regName, regUsername, regEmail, regPassword;

    private FirebaseAuth mAuth = Session.mAuth;
    private FirebaseFirestore db = Session.db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        //Hooks to all xml elements in activity_sign_up.xml

        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        go = findViewById(R.id.gotoDashboard);

        go.setOnClickListener(view -> {
            //rootNode = FirebaseDatabase.getInstance();
            // reference = rootNode.getReference("users");

            //Get all the values

            String name = Objects.requireNonNull(regName.getText()).toString();
            String username = Objects.requireNonNull(regUsername.getText()).toString();
            String email = Objects.requireNonNull(regEmail.getText()).toString();
            String password = Objects.requireNonNull(regPassword.getText()).toString();


            proccessRegister(name, username, email, password);

            //   if(name.isEmpty()){
            //        regName.setError("Full Name is Required");
            //       return;
            //     }

            //     if(username.isEmpty()){
            //        regUsername.setError("User Name is Required");
            //         return;
            //     }

            //     if(email.isEmpty()){
            //          regEmail.setError("Email is Required");
            //          return;
            //      }

            //      if(password.isEmpty()){
            //          regPassword.setError("Password is Required");
            //          return;
            //       }

            // data is validated
            //register the user using firebase


            // UserHelperClass helperClass = new UserHelperClass(name, username, email, password);

            //  reference.child(username).setValue(helperClass);

            //  Intent intent= new Intent(SignUp.this, Dashboard.class);
            //  startActivity(intent);
            //  finish();

        });
    }

    //Save all data in FireBase on button click

    public void proccessRegister(String full_name, String username, String email, String password) {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put("full_name", full_name);
        user.put("username", username);
        user.put("email", email);
        user.put("password", password);

        DocumentReference docIdRef = db.getInstance().collection("users").document(username);


        docIdRef.get().addOnCompleteListener(firestore_task -> {
            if (firestore_task.isSuccessful()) {
                DocumentSnapshot document = firestore_task.getResult();
                if (!document.exists()) {
                    mAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {

                            //We should not save the password in the json object present in firestore for security reasons
                            user.remove("password");
                            docIdRef.set(user);
                            IBinder token = findViewById(android.R.id.content).getWindowToken();
                            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            mgr.hideSoftInputFromWindow(token, 0);
                            startActivity(new Intent(this, Login.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}