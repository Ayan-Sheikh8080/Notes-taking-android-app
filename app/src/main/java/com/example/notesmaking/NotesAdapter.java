package com.example.notesmaking;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Adapter class for managing and displaying notes in a RecyclerView
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    // Interface for handling note deletion events
    interface DeleteNoteListener {
        void deleteNote(String note);
    }

    // Dataset containing notes
    private ArrayList<String> notesList;

    // Listener for delete note events
    private DeleteNoteListener deleteNoteListener;

    // Constructor to initialize the adapter with a dataset
    public NotesAdapter(ArrayList<String> dataSet) {
        notesList = dataSet;
    }

    // Method to set the delete note listener
    public void setDeleteNoteListener(DeleteNoteListener deleteNoteListener) {
        this.deleteNoteListener = deleteNoteListener;
    }

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_card, viewGroup, false);

        // Return a new NoteViewHolder instance for the created view
        return new NoteViewHolder(view, deleteNoteListener);
    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, final int position) {
        // Get the cardView from the NoteViewHolder
        CardView cardView = noteViewHolder.getCardView();

        // Get the TextView inside the cardView and set its text to the corresponding note
        TextView noteTextView = cardView.findViewById(R.id.noteText);
        noteTextView.setText(notesList.get(position));
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notesList.size();
    }

    // ViewHolder class representing each item in the RecyclerView
    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // CardView to hold the note
        private final CardView cardView;

        // Listener for delete note events
        private DeleteNoteListener deleteNoteListener;

        // Constructor to initialize the NoteViewHolder with a view and delete note listener
        public NoteViewHolder(View view, DeleteNoteListener deleteNoteListener) {
            super(view);

            // Set the delete note listener
            this.deleteNoteListener = deleteNoteListener;

            // Get the CardView from the view and set a context menu listener
            cardView = (CardView) view;
            cardView.setOnCreateContextMenuListener(this);
        }

        // Getter for the CardView
        public CardView getCardView() {
            return cardView;
        }

        // Called when the context menu is being built
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // Add a "Delete" option to the context menu
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            // Set a click listener for the "Delete" option
            delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    // Get the note text from the TextView inside the CardView
                    TextView noteTextView = cardView.findViewById(R.id.noteText);

                    // Notify the delete note listener about the deletion
                    deleteNoteListener.deleteNote(noteTextView.getText().toString());
                    return false;
                }
            });
        }
    }
}
