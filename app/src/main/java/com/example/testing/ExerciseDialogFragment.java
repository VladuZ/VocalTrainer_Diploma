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
import java.io.File;

public class ExerciseDialogFragment extends DialogFragment {

    private static final String ARG_EXERCISE_NAME = "exerciseName";
    private static final String ARG_EXERCISE_BPM = "exerciseBpm";

    private String exerciseName;
    private int exerciseBpm;
    private OnExerciseDeletedListener listener;

    public static ExerciseDialogFragment newInstance(String name, int bpm, OnExerciseDeletedListener listener) {
        ExerciseDialogFragment fragment = new ExerciseDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXERCISE_NAME, name);
        args.putInt(ARG_EXERCISE_BPM, bpm);
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
        Button backButton = view.findViewById(R.id.backButton);

        exerciseNameTextView.setText(exerciseName);
        exerciseBPMTextView.setText(String.format("BPM: %d", exerciseBpm));

        playButton.setOnClickListener(v -> {
            // Handle play button click
            dismiss();
        });

        editButton.setOnClickListener(v -> {
            // Handle edit button click
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

        backButton.setOnClickListener(v -> {
            // Handle back button click
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
                    // Optionally, show a toast or message indicating successful deletion
                } else {
                    // Optionally, show a toast or message indicating deletion failure
                }
            }
        }
    }
}
