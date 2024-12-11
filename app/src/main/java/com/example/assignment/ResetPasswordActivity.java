package com.example.assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailInput);
        resetButton = findViewById(R.id.resetPasswordBtn);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send reset password email
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // If the email is valid and the reset request is sent
                                Toast.makeText(ResetPasswordActivity.this, "Password reset email sent. Please check your inbox.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle error scenarios: email may not exist in Firebase Authentication
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email. Ensure the email is registered with us.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}