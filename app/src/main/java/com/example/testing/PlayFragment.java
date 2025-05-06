package com.example.testing;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class PlayFragment extends Fragment {

    private NoteAnimator noteAnimator;
    private List<Note> notesList = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        noteAnimator = view.findViewById(R.id.noteAnimator);

        // Retrieve notes data from arguments
        if (getArguments() != null) {
            notesList = (List<Note>) getArguments().getSerializable("notesList");
            playNotes();
        }

        return view;
    }

    private void playNotes() {
        int currentDelay = 0;

        for (Note note : notesList) {
            int pitchIndex = convertNoteToPitchIndex(note.getContent());
            int length = note.getLength();
            int position = note.getPosition();

            int duration = calculateNoteDuration(length);

            handler.postDelayed(() -> noteAnimator.addNote(pitchIndex, length), currentDelay + position * 1000);
            currentDelay += duration;
        }
    }

    private int convertNoteToPitchIndex(String note) {
        // Convert note string (e.g., "C1", "D#3") to pitch index
        int octave = Character.getNumericValue(note.charAt(note.length() - 1));
        String noteName = note.substring(0, note.length() - 1);

        int basePitch = 0;
        switch (noteName) {
            case "C": basePitch = 0; break;
            case "C#": basePitch = 1; break;
            case "D": basePitch = 2; break;
            case "D#": basePitch = 3; break;
            case "E": basePitch = 4; break;
            case "F": basePitch = 5; break;
            case "F#": basePitch = 6; break;
            case "G": basePitch = 7; break;
            case "G#": basePitch = 8; break;
            case "A": basePitch = 9; break;
            case "A#": basePitch = 10; break;
            case "B": basePitch = 11; break;
        }

        return basePitch + (octave - 1) * 12;
    }

    private int calculateNoteDuration(int length) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int distance = screenWidth + length;
        int frames = distance / NoteAnimator.NOTE_SPEED;
        return frames * NoteAnimator.REFRESH_RATE;
    }
}
