package com.example.testing;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExerciseEditorFragment extends Fragment {

    private EditText exerciseNameInput;
    private EditText noteInput;
    private EditText notePositionInput;
    private EditText noteLengthInput;
    private Button addNoteButton;
    private Button playButton;
    private Button saveButton;
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;

    private static final Pattern NOTE_PATTERN = Pattern.compile("^[A-G][#b]?[0-9]$");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_editor, container, false);

        exerciseNameInput = view.findViewById(R.id.exerciseNameInput);
        noteInput = view.findViewById(R.id.noteInput);
        notePositionInput = view.findViewById(R.id.notePositionInput);
        noteLengthInput = view.findViewById(R.id.noteLengthInput);
        addNoteButton = view.findViewById(R.id.addNoteButton);
        playButton = view.findViewById(R.id.playButton);
        saveButton = view.findViewById(R.id.saveButton);
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList);
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addNoteButton.setOnClickListener(v -> addNote());
        playButton.setOnClickListener(v -> openPianoPlayFragment());
        saveButton.setOnClickListener(v -> saveNotes());

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void addNote() {
        String noteContent = noteInput.getText().toString().trim();
        String positionText = notePositionInput.getText().toString().trim();
        String lengthText = noteLengthInput.getText().toString().trim();

        if (isValidNote(noteContent) && !TextUtils.isEmpty(positionText) && !TextUtils.isEmpty(lengthText)) {
            int position = Integer.parseInt(positionText);
            int length = Integer.parseInt(lengthText);

            if (isPositionAvailable(position, length)) {
                Note note = new Note(noteContent, position, length);
                notesList.add(note);
                notesAdapter.notifyItemInserted(notesList.size() - 1);
                clearInputFields();
            } else {
                Toast.makeText(getContext(), "Note overlaps with existing notes.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Invalid input. Please check the note format and values.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidNote(String note) {
        return NOTE_PATTERN.matcher(note).matches();
    }

    private boolean isPositionAvailable(int position, int length) {
        for (Note note : notesList) {
            int existingStart = note.getPosition();
            int existingEnd = existingStart + note.getLength();
            int newEnd = position + length;
            if (!(position >= existingEnd || newEnd <= existingStart)) {
                return false;
            }
        }
        return true;
    }

    private void clearInputFields() {
        noteInput.setText("");
        notePositionInput.setText("");
        noteLengthInput.setText("");
    }

    private void openPianoPlayFragment() {
        if (!notesList.isEmpty()) {
            Fragment pianoPlayFragment = new PlayFragment();
            Bundle args = new Bundle();
            args.putSerializable("notesList", (ArrayList<Note>) notesList);
            pianoPlayFragment.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, pianoPlayFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "Please add notes before playing.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNotes() {
        // Implement logic to save notes
        String exerciseName = exerciseNameInput.getText().toString().trim();
        if (exerciseName.isEmpty()) {
            Toast.makeText(getContext(), "Please enter an exercise name.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Exercise saved: " + exerciseName, Toast.LENGTH_SHORT).show();
            // Here you would typically save the notesList to persistent storage
        }
    }
}
