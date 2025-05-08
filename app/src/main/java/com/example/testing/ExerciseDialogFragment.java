package com.example.testing;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDialogFragment extends DialogFragment {

    private static final String ARG_EXERCISE_NAME = "exerciseName";
    private static final String ARG_EXERCISE_BPM = "exerciseBpm";
    private static final String ARG_EXERCISE_CELLS = "exerciseCells";

    private String exerciseName;
    private int exerciseBpm;
    private List<GridView.Cell> exerciseCells;
    private OnExerciseDeletedListener listener;

    public static ExerciseDialogFragment newInstance(String name, int bpm, List<GridView.Cell> cells, OnExerciseDeletedListener listener) {
        ExerciseDialogFragment fragment = new ExerciseDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXERCISE_NAME, name);
        args.putInt(ARG_EXERCISE_BPM, bpm);
        args.putSerializable(ARG_EXERCISE_CELLS, new ArrayList<>(cells));
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(OnExerciseDeletedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseName = getArguments().getString(ARG_EXERCISE_NAME);
            exerciseBpm = getArguments().getInt(ARG_EXERCISE_BPM);
            exerciseCells = (List<GridView.Cell>) getArguments().getSerializable(ARG_EXERCISE_CELLS);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_exercise, null);

        TextView exerciseNameTextView = view.findViewById(R.id.exerciseNameTextView);
        TextView exerciseBPMTextView = view.findViewById(R.id.exerciseBPMTextView);
        Button playButton = view.findViewById(R.id.playButton);
        Button editButton = view.findViewById(R.id.editButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        exerciseNameTextView.setText(exerciseName);
        exerciseBPMTextView.setText(String.format("BPM: %d", exerciseBpm));

        playButton.setOnClickListener(v -> {
            // Handle play button click
            PlayFragment playFragment = new PlayFragment();

            // Pass the selected cells to the editor fragment
            Bundle args = new Bundle();
            args.putSerializable("selectedCells", new ArrayList<>(exerciseCells));
            playFragment.setArguments(args);

            // Replace the current fragment with the editor fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, playFragment)
                    .addToBackStack(null)
                    .commit();

            dismiss();
        });

        editButton.setOnClickListener(v -> {
            // Create a new instance of ExerciseEditorFragment
            ExerciseEditorFragment editorFragment = new ExerciseEditorFragment();

            // Pass the selected cells to the editor fragment
            Bundle args = new Bundle();
            args.putSerializable("selectedCells", new ArrayList<>(exerciseCells));
            editorFragment.setArguments(args);

            // Replace the current fragment with the editor fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, editorFragment)
                    .addToBackStack(null)
                    .commit();

            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            // Handle delete button click
            deleteExerciseFile();
            if (listener != null) {
                listener.onExerciseDeleted();
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }

    private void deleteExerciseFile() {
        Context context = getContext();
        if (context != null) {
            File file = new File(context.getFilesDir(), exerciseName + ".json");
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    Toast.makeText(context, "Exercise succesfully deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to delete exercise", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
