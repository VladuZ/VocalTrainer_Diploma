package com.example.testing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;
    private FragmentManager fragmentManager;
    private OnExerciseDeletedListener listener;

    public ExerciseAdapter(List<Exercise> exercises, FragmentManager fragmentManager, OnExerciseDeletedListener listener) {
        this.exercises = exercises;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.exerciseNameTextView.setText(exercise.getName());
        holder.exerciseBPMTextView.setText(String.format("BPM: %d", exercise.getBpm()));

        holder.itemView.setOnClickListener(v -> {
            DialogFragmentExercise dialogFragment = DialogFragmentExercise.newInstance(exercise.getName(), exercise.getBpm(), exercise.getCells(), listener);
            dialogFragment.show(fragmentManager, "ExerciseDialogFragment");
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseNameTextView;
        TextView exerciseBPMTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            exerciseBPMTextView = itemView.findViewById(R.id.exerciseBPMTextView);
        }
    }
}
