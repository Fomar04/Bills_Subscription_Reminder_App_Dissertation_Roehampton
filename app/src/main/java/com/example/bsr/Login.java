package com.example.bsr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;


public class Login extends AppCompatActivity {

    private Button go;
    private TextView loginUsername, loginPass, signUP, forgetPass;

    private FirebaseAuth mAuth = Session.mAuth;
    private FirebaseFirestore db = Session.db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        signUP = findViewById(R.id.loginToSignUp);

        loginUsername = findViewById(R.id.loginUsername);
        loginPass = findViewById(R.id.loginPass);

        signUP.setOnClickListener(view -> {

            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
            finish();
        });

        go = findViewById(R.id.gotoDashboard);

        go.setOnClickListener(view -> {
            String username = loginUsername.getText().toString().trim();
            String password = loginPass.getText().toString().trim();

            processLogin(username, password);
        });

        forgetPass = findViewById(R.id.loginForgotPass);

        //forgetPass.setOnClickListener(view -> {
        //  Intent intent= new Intent(Login.this, Reset.class);
        //  startActivity(intent);
        //  finish();
        // });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and sent the reset link

                        if (!resetMail.getText().toString().isEmpty()){
                            mAuth.getInstance().sendPasswordResetEmail(resetMail.getText().toString());
                            Toast.makeText(Login.this, "Password link sent!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Login.this, "The email cannot be empty", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close the dialog

                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }

    private void processLogin(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_LONG).show();

            return;
        }

        DocumentReference docRef = db.getInstance().collection("users").document(username);

        docRef.get().addOnCompleteListener(find_task -> {
            if (find_task.isSuccessful()) {

                if (find_task.getResult().get("email") != null) {
                    String email = find_task.getResult().get("email").toString();

                    mAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(login_task -> {
                        if (login_task.isSuccessful()) {

                            db.getInstance().collection("users").document(username).get().addOnCompleteListener(db_task -> {
                                if (db_task.isSuccessful()) {
                                    //After logout userdata will go null for some reason so we reinitialize it
                                    Session.userData = new HashMap<>();

                                    Session.authenticatedUser = mAuth.getInstance().getCurrentUser();
                                    Session.userData.putAll(db_task.getResult().getData());

                                    if (!Session.userData.isEmpty() && Session.authenticatedUser.getUid() != null) {
                                        //Keyboard retracts
                                        IBinder token = findViewById(android.R.id.content).getWindowToken();
                                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        mgr.hideSoftInputFromWindow(token, 0);

                                        startActivity(new Intent(this, Dashboard.class));
                                    } else {
                                        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });


    }


}