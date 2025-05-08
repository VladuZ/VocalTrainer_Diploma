package com.example.testing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        // Retrieve the selected cells from the arguments
        Bundle args = getArguments();
        if (args != null) {
            List<GridView.Cell> selectedCells = (List<GridView.Cell>) args.getSerializable("selectedCells");
            Collections.sort(selectedCells, Comparator.comparingInt(v -> v.col));

            int prevNotePos = 0;
            int currentDelay = 0;

            /*for (int i = 0; i < selectedCells.size(); i++) {
                final int pitchIndex = selectedCells.get(i).row;
                int length = 100;
                int duration = calculateNoteDuration(length);

                handler.postDelayed(() -> noteAnimator.addNote(pitchIndex, length), currentDelay);
                currentDelay += duration;
            }*/
        }

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }
}
