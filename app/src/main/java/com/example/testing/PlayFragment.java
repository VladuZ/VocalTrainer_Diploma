package com.example.testing;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class PlayFragment extends Fragment {

    private PianoView pianoView;
    private NoteAnimator noteAnimator;
    private List<Note> notesList;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        pianoView = view.findViewById(R.id.pianoView);
        noteAnimator = view.findViewById(R.id.noteAnimator);

        // Retrieve the list of notes passed from ExerciseEditorFragment
        if (getArguments() != null) {
            notesList = (List<Note>) getArguments().getSerializable("notesList");
            playNotes();
        }

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void playNotes() {
        int currentDelay = 0;
        for (Note note : notesList) {
            int pitchIndex = note.getPosition(); // Assuming position corresponds to pitchIndex
            int length = note.getLength();
            int duration = calculateNoteDuration(length);

            handler.postDelayed(() -> noteAnimator.addNote(pitchIndex, length), currentDelay);
            currentDelay += duration;
        }
    }

    private int calculateNoteDuration(int lengthPx) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int distance = screenWidth + lengthPx;
        int frames = distance / NoteAnimator.NOTE_SPEED;
        return frames * NoteAnimator.REFRESH_RATE;
    }
}
