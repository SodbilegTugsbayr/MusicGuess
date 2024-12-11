package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class EndGameActivity extends AppCompatActivity {

    private TextView finalScoreTextView;
    private Button restartButton, exitButton;

    // Firebase Database Reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        finalScoreTextView = findViewById(R.id.finalScoreTextView);
        restartButton = findViewById(R.id.restartButton);
        exitButton = findViewById(R.id.exitButton);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        int finalScore = getIntent().getIntExtra("finalScore", 0);
        int songCount = getIntent().getIntExtra("songCount", 0);
        String uid = getIntent().getStringExtra("uid");

        finalScoreTextView.setText("Таны авсан оноо : " + finalScore);

        // Push data to Firebase
        if (uid != null) {
            pushHistoryToFirebase(uid, finalScore, songCount);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        restartButton.setOnClickListener(v -> restartGame());

        exitButton.setOnClickListener(v -> exitGame());
    }

    private void pushHistoryToFirebase(String uid, int finalScore, int songCount) {
        // Create a new history entry
        Map<String, Object> historyEntry = new HashMap<>();
        historyEntry.put("finalScore", finalScore);
        historyEntry.put("songCount", songCount);

        // Push the history entry to the user's history node
        databaseReference.child(uid).child("history").push().setValue(historyEntry)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(EndGameActivity.this, "History updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(EndGameActivity.this, "Failed to update history: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void restartGame() {
        Intent intent = new Intent(EndGameActivity.this, MenuActivity.class);
        intent.putExtra("uid", getIntent().getStringExtra("uid"));
        startActivity(intent);
        finish();
    }

    private void exitGame() {
        finish();
    }
}