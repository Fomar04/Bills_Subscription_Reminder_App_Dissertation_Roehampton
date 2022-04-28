package com.example.bsr;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Session {
    public static FirebaseUser authenticatedUser;
    public static Map<String, Object> userData = new HashMap<>();
    public static FirebaseAuth mAuth;
    public static FirebaseFirestore db;
}
