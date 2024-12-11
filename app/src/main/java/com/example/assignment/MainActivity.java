package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnStartGame, btnExit;
    ListView historyListView;
    DatabaseReference databaseReference;
    ArrayList<String> historyList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uid = getIntent().getStringExtra("uid");

        btnStartGame = findViewById(R.id.btnStartGame);
        btnExit = findViewById(R.id.btnExit);
        historyListView = findViewById(R.id.historyListView);

        historyList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        historyListView.setAdapter(arrayAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (uid != null) {
            fetchUserHistory(uid);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        btnStartGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });
    }

    private void fetchUserHistory(String uid) {
        databaseReference.child(uid).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                    Map<String, Object> historyMap = (Map<String, Object>) historySnapshot.getValue();
                    if (historyMap != null) {
                        int finalScore = ((Long) historyMap.get("finalScore")).intValue();
                        int songCount = ((Long) historyMap.get("songCount")).intValue();
                        historyList.add("Оноо: " + finalScore + ", Дууны тоо: " + songCount);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch history: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}