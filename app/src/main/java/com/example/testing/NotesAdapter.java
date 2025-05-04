package com.example.testing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notesList;

    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.noteText.setText(note.getContent());
        holder.noteDetails.setText("Position: " + note.getPosition() + ", Length: " + note.getLength());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteText;
        TextView noteDetails;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText = itemView.findViewById(android.R.id.text1);
            noteDetails = itemView.findViewById(android.R.id.text2);
        }
    }
}
