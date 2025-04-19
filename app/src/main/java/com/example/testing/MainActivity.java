package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private PianoView pianoView;
    private NoteAnimator noteAnimator;
    private Handler handler = new Handler();
    private Random random = new Random();
    private TextView noteTextView;

    private AudioAnalyzer audioAnalyzer;

    private List<Integer> noteList = new ArrayList<>();
    private int currentNoteIndex = -1;

    private final String[] NOTE_NAMES = {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestMicrophonePermission();

        pianoView = findViewById(R.id.pianoView);
        noteAnimator = findViewById(R.id.noteAnimator);
        noteTextView = findViewById(R.id.noteText);

        audioAnalyzer = new AudioAnalyzer(this);

        for (int i = 0; i < 20; i++) {
            int pitchIndex = random.nextInt(36);
            noteList.add(pitchIndex);
        }

        int currentDelay = 0;

        for (int i = 0; i < noteList.size(); i++) {
            final int pitchIndex = noteList.get(i);
            int length = 100 + random.nextInt(200);
            int duration = calculateNoteDuration(length);

            handler.postDelayed(() -> noteAnimator.addNote(pitchIndex, length), currentDelay);
            currentDelay += duration;
        }

        startNoteTextUpdater();
    }

    private void requestMicrophonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void startNoteTextUpdater() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentPitch = noteAnimator.getCurrentPitchIndex();
                int foundIndex = findCurrentNoteIndex(currentPitch);
                int sungPitch = audioAnalyzer.getCurrentPitchIndex();

                if (foundIndex != currentNoteIndex) {
                    currentNoteIndex = foundIndex;
                }

                int nextPitch = (currentNoteIndex + 1 < noteList.size()) ? noteList.get(currentNoteIndex + 1) : -1;

                updateNoteText(currentPitch, nextPitch, sungPitch);

                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    private int findCurrentNoteIndex(int currentPitch) {
        for (int i = 0; i < noteList.size(); i++) {
            if (noteList.get(i) == currentPitch) {
                return i;
            }
        }
        return -1;
    }

    private void updateNoteText(int currentPitch, int nextPitch, int sungPitch) {
        String currentNote = getNoteName(currentPitch);
        String nextNote = getNoteName(nextPitch);
        String sungNote = getNoteName(sungPitch);

        String accuracy = (sungPitch != -1 && sungPitch == currentPitch) ? "✔️" :
                (sungPitch != -1 && currentPitch != -1) ? "❌" : "—";

        String display = "Now: " + currentNote +
                "\nNext: " + nextNote +
                "\nYou: " + sungNote +
                "\nMatch: " + accuracy;

        noteTextView.setText(display);
    }

    private String getNoteName(int pitchIndex) {
        if (pitchIndex == -1) return "—";
        int octave = pitchIndex / 12;
        int note = pitchIndex % 12;
        return NOTE_NAMES[note] + octave;
    }

    private int calculateNoteDuration(int lengthPx) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int distance = screenWidth + lengthPx;
        int frames = distance / NoteAnimator.NOTE_SPEED;
        return frames * NoteAnimator.REFRESH_RATE;
    }
}
