package com.example.testing;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentHome extends Fragment implements OnExerciseDeletedListener {

    private ExerciseAdapter adapter;
    private List<Exercise> exercises;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button createExerciseButton = view.findViewById(R.id.createExerciseButton);
        Button settingsButton = view.findViewById(R.id.settingsButton);
        RecyclerView exercisesRecyclerView = view.findViewById(R.id.exercisesRecyclerView);

        createExerciseButton.setOnClickListener(v -> {
            Fragment exerciseFragment = new FragmentExerciseEditor();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, exerciseFragment)
                    .addToBackStack(null)
                    .commit();
        });

        settingsButton.setOnClickListener(v -> {
            Fragment settingsFragment = new FragmentAccount();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, settingsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Initialize the list and adapter
        exercises = readExercises();
        adapter = new ExerciseAdapter(exercises, getParentFragmentManager(), this);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exercisesRecyclerView.setAdapter(adapter);

        return view;
    }

    private List<Exercise> readExercises() {
        List<Exercise> exercises = new ArrayList<>();
        File filesDir = requireContext().getFilesDir();
        File[] files = filesDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();

                    String jsonString = new String(data);
                    JSONObject jsonObject = new JSONObject(jsonString);

                    String exerciseName = jsonObject.getString("exerciseName");
                    int bpm = jsonObject.getInt("bpm");
                    JSONArray notesArray = jsonObject.getJSONArray("notes");

                    List<ViewGrid.Cell> cells = new ArrayList<>();
                    for (int i = 0; i < notesArray.length(); i++) {
                        JSONObject note = notesArray.getJSONObject(i);
                        int row = note.getInt("row");
                        int col = note.getInt("column");
                        cells.add(new ViewGrid.Cell(row, col));
                    }

                    exercises.add(new Exercise(exerciseName, bpm, cells));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return exercises;
    }

    @Override
    public void onExerciseDeleted() {
        // Refresh the exercise list
        exercises.clear();
        exercises.addAll(readExercises());
        adapter.notifyDataSetChanged();
    }
}
