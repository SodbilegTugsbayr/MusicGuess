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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        final EditText name = findViewById(R.id.name);
        final EditText email = findViewById(R.id.email);
        final EditText mobile = findViewById(R.id.mobile);
        final EditText password = findViewById(R.id.password);
        final EditText conpassword = findViewById(R.id.conpassword);
        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNow = findViewById(R.id.loginNow);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameTxt = name.getText().toString();
                final String emailTxt = email.getText().toString();
                final String mobileTxt = mobile.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conpasswordTxt = conpassword.getText().toString();

                if (nameTxt.isEmpty() || emailTxt.isEmpty() || mobileTxt.isEmpty() || passwordTxt.isEmpty() || conpasswordTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(conpasswordTxt)) {
                    Toast.makeText(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {
                    // Create user in Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                            .addOnCompleteListener(Register.this, task -> {
                                if (task.isSuccessful()) {
                                    // User created successfully, now store additional data in Realtime Database
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        String userId = firebaseUser.getUid();
                                        databaseReference.child(userId).child("name").setValue(nameTxt);
                                        databaseReference.child(userId).child("email").setValue(emailTxt);
                                        databaseReference.child(userId).child("mobile").setValue(mobileTxt);
                                        databaseReference.child(userId).child("role").setValue("user");

                                        Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register.this, Login.class));
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        loginNow.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }
}