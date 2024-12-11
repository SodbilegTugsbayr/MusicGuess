package com.example.assignment;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView, songsRecyclerView;
    private UserAdapter userAdapter;
    private SongAdapter songAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        songsRecyclerView = findViewById(R.id.songsRecyclerView);

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(new ArrayList<>());
        songAdapter = new SongAdapter(new ArrayList<>());

        usersRecyclerView.setAdapter(userAdapter);
        songsRecyclerView.setAdapter(songAdapter);

        fetchUsers();
        fetchSongs();
    }

    private void fetchUsers() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);
                    userList.add(new User(name, email, role));
                }
                userAdapter.updateUsers(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error fetching users: " + databaseError.getMessage());
            }
        });
    }

    private void fetchSongs() {
        databaseReference.child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> songList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String song = snapshot.getValue(String.class);
                    songList.add(song);
                }
                for (String song : songList){
                    Log.e("AdminActivity", "Song: " + song);
                }
                songAdapter.updateSongs(songList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error fetching songs: " + databaseError.getMessage());
            }
        });
    }
}