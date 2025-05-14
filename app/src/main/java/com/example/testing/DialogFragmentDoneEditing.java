package com.example.testing;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DialogFragmentDoneEditing extends DialogFragment {

    private EditText exerciseNameEditText;
    private EditText exerciseBPMEditText;
    private Button buttonOk;
    private List<ViewGrid.Cell> selectedCells;

    public DialogFragmentDoneEditing(List<ViewGrid.Cell> selectedCells) {
        super();
        this.selectedCells = selectedCells;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_done_editing, container, false);

        exerciseNameEditText = view.findViewById(R.id.nameText);
        exerciseBPMEditText = view.findViewById(R.id.bpmText);
        buttonOk = view.findViewById(R.id.buttonOk);

        exerciseNameEditText.setFilters(new InputFilter[]{new InputFilterExerciseName()});
        exerciseBPMEditText.setFilters(new InputFilter[]{new InputFilterExerciseBPM()});

        buttonOk.setOnClickListener(v -> {
            Context context = getActivity();
            if (context == null) {
                return;
            }

            String exerciseName = exerciseNameEditText.getText().toString();
            String bpmText = exerciseBPMEditText.getText().toString();

            if (exerciseName.isEmpty() || bpmText.isEmpty()) {
                Toast.makeText(context, "Exercise name and BPM cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }

            int bpm;
            try {
                bpm = Integer.parseInt(bpmText);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid BPM value", Toast.LENGTH_LONG).show();
                return;
            }

            if (bpm < 60 || bpm > 300) {
                Toast.makeText(context, "BPM should be in range from 60 to 300", Toast.LENGTH_LONG).show();
                return;
            }

            // Save to JSON file
            saveToJsonFile(context, exerciseName, bpm);
        });
        return view;
    }

    private void saveToJsonFile(Context context, String exerciseName, int bpm) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("exerciseName", exerciseName);
            jsonObject.put("bpm", bpm);

            // Create a JSONArray to store cell details
            JSONArray cellsArray = new JSONArray();
            for (ViewGrid.Cell cell : selectedCells) {
                JSONObject cellObject = new JSONObject();
                cellObject.put("row", cell.row);
                cellObject.put("column", cell.col);
                cellsArray.put(cellObject);
            }
            jsonObject.put("notes", cellsArray);

            // Check if file already exists
            File file = new File(context.getFilesDir(), exerciseName + ".json");
            if (file.exists()) {
                Toast.makeText(context, "Exercise with this name already exists", Toast.LENGTH_LONG).show();
                return;
            }

            // Write to file
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            // Display the file directory
            String filePath = file.getAbsolutePath();
            Toast.makeText(context, "Exercise saved successfully at: " + filePath, Toast.LENGTH_LONG).show();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving exercise", Toast.LENGTH_LONG).show();
        }
    }
}
