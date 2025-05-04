package com.example.testing;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button createExerciseButton = view.findViewById(R.id.createExerciseButton);
        Button settingsButton = view.findViewById(R.id.settingsButton);

        createExerciseButton.setOnClickListener(v -> {
            Fragment exerciseFragment = new ExerciseEditorFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, exerciseFragment)
                    .addToBackStack(null)
                    .commit();
        });

        settingsButton.setOnClickListener(v -> {
            Fragment settingsFragment = new SettingsFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, settingsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
