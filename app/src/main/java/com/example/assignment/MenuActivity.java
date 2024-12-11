package com.example.assignment; // Use your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String uid = getIntent().getStringExtra("uid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btnBack = findViewById(R.id.btnBack);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity(5, uid);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity(10, uid);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity(15, uid);  // Pass 15 songs to the GameActivity
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startGameActivity(int songCount, String uid) {
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        intent.putExtra("songCount", songCount);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}
