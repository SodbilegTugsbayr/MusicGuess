package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);
        final TextView forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();

                if (emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Sign in with Firebase Authentication
                    mAuth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                            .addOnCompleteListener(Login.this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        String userId = firebaseUser.getUid();
                                        fetchUserRole(userId);
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        registerNowBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
            finish();
        });

        forgotPasswordLink.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, ResetPasswordActivity.class));
        });
    }

    private void fetchUserRole(String userId) {
        databaseReference.child(userId).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.getValue(String.class);
                    Toast.makeText(Login.this, "Welcome, your role is: " + role, Toast.LENGTH_SHORT).show();

                    // Navigate based on role
                    if ("admin".equals(role)) {
                        startActivity(new Intent(Login.this, AdminActivity.class));
                    } else if ("user".equals(role)) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("uid", userId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Role not recognized", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(Login.this, "Role not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Failed to fetch role: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}