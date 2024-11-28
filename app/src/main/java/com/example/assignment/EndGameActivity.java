package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EndGameActivity extends AppCompatActivity {

    private TextView finalScoreTextView;
    private Button restartButton, exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        finalScoreTextView = findViewById(R.id.finalScoreTextView);
        restartButton = findViewById(R.id.restartButton);
        exitButton = findViewById(R.id.exitButton);

        int finalScore = getIntent().getIntExtra("finalScore", 0);

        finalScoreTextView.setText("Таны авсан оноо : " + finalScore);

        restartButton.setOnClickListener(v -> restartGame());

        exitButton.setOnClickListener(v -> exitGame());
    }

    private void restartGame() {
        Intent intent = new Intent(EndGameActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void exitGame() {
        finish();
    }
}
