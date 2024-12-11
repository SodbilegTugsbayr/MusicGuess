package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.media.MediaPlayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView scoreTextView;
    private Button musicOption1;
    private Button musicOption2;
    private Button musicOption3;
    private int score = 0;
    private List<String> songList;
    private List<Integer> songResources;
    private List<String> usedSongs;
    private String currentSong;
    private int songCount;
    private int currentRound = 0;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreTextView = findViewById(R.id.scoreTextView);
        Button playButton = findViewById(R.id.playButton);
        Button stopButton = findViewById(R.id.stopButton);
        musicOption1 = findViewById(R.id.musicOption1);
        musicOption2 = findViewById(R.id.musicOption2);
        musicOption3 = findViewById(R.id.musicOption3);
        Button nextButton = findViewById(R.id.nextButton);

        songCount = getIntent().getIntExtra("songCount", 0);
        loadSongList();
        usedSongs = new ArrayList<>();

        startGame();
        currentRound++;

        playButton.setOnClickListener(v -> playSong());
        stopButton.setOnClickListener(v -> stopSong());

        nextButton.setOnClickListener(v -> {
            if (currentRound < songCount) {
                currentRound++;
                startGame();
            } else {
                mediaPlayer.stop();
                mediaPlayer.release();
                navigateToEndGameActivity();
            }
        });
    }

    private void loadSongList() {
        String[] songNamesArray = getResources().getStringArray(R.array.song_names);

        songList = new ArrayList<>();
        Collections.addAll(songList, songNamesArray);

        songResources = new ArrayList<>();
        for (String songName : songNamesArray) {
            int resId = getResources().getIdentifier(songName, "raw", getPackageName());
            if (resId != 0) {
                songResources.add(resId);
            }
        }
    }

    private void startGame() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        currentSong = getRandomSong();
        playSong();

        List<String> options = new ArrayList<>();
        options.add(currentSong);

        while (options.size() < 3) {
            String randomSong = getRandomSong();
            if (!options.contains(randomSong)) {
                options.add(randomSong);
            }
        }

        Collections.shuffle(options);

        musicOption1.setText(options.get(0));
        musicOption2.setText(options.get(1));
        musicOption3.setText(options.get(2));

        resetButtonColors();

        musicOption1.setOnClickListener(v -> checkAnswer(options.get(0), musicOption1));
        musicOption2.setOnClickListener(v -> checkAnswer(options.get(1), musicOption2));
        musicOption3.setOnClickListener(v -> checkAnswer(options.get(2), musicOption3));
    }

    private String getRandomSong() {
        Random random = new Random();
        String randomSong;

        do {
            int randomIndex = random.nextInt(songList.size());
            randomSong = songList.get(randomIndex);
        } while (usedSongs.contains(randomSong));

        usedSongs.add(randomSong);
        return randomSong;
    }

    private void playSong() {
        int songResource = getSongResource(currentSong);
        mediaPlayer = MediaPlayer.create(this, songResource);
        mediaPlayer.start();
    }

    private void stopSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private int getSongResource(String songName) {
        int index = songList.indexOf(songName);
        if (index != -1 && songResources.size() > index) {
            return songResources.get(index);
        }
        return R.raw.seigfried;
    }

    private void checkAnswer(String selectedOption, Button selectedButton) {
        if (selectedOption.equals(currentSong)) {
            score++;
            selectedButton.setBackgroundColor(Color.GREEN);
        } else {
            selectedButton.setBackgroundColor(Color.RED);
            if (musicOption1.getText().toString().equals(currentSong)) {
                musicOption1.setBackgroundColor(Color.GREEN);
            } else if (musicOption2.getText().toString().equals(currentSong)) {
                musicOption2.setBackgroundColor(Color.GREEN);
            } else if (musicOption3.getText().toString().equals(currentSong)) {
                musicOption3.setBackgroundColor(Color.GREEN);
            }
        }

        scoreTextView.setText("Оноо: " + score);

        musicOption1.setEnabled(false);
        musicOption2.setEnabled(false);
        musicOption3.setEnabled(false);
    }

    private void resetButtonColors() {
        musicOption1.setBackgroundColor(Color.LTGRAY);
        musicOption2.setBackgroundColor(Color.LTGRAY);
        musicOption3.setBackgroundColor(Color.LTGRAY);

        musicOption1.setEnabled(true);
        musicOption2.setEnabled(true);
        musicOption3.setEnabled(true);
    }

    // Method to navigate to the EndGameActivity after the game ends
    private void navigateToEndGameActivity() {
        Intent intent = new Intent(GameActivity.this, EndGameActivity.class);  // Specify the EndGameActivity
        intent.putExtra("finalScore", score);
        intent.putExtra("songCount", songCount);
        intent.putExtra("uid", getIntent().getStringExtra("uid"));
        startActivity(intent);
        finish();
    }
}
