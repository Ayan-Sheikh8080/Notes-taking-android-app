package com.example.notesmaking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NotesAdapter.DeleteNoteListener {
    private EditText noteEditText;
    private SharedPreferences notePreferences;
    private ArrayList<String> noteList;
    private RecyclerView noteRecyclerView;
    private NotesAdapter noteAdapter;
    private Button saveNoteButton;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        mainLayout = findViewById(R.id.mainLayout);
        noteEditText = findViewById(R.id.editTextNote);
        noteRecyclerView = findViewById(R.id.notesRecycler);
        saveNoteButton = findViewById(R.id.saveBtn);

        // Initialize SharedPreferences for storing notes
        notePreferences = getSharedPreferences("notes", Context.MODE_PRIVATE);

        // Initialize notes ArrayList and adapter
        noteList = new ArrayList<>();
        noteAdapter = new NotesAdapter(noteList);
        noteAdapter.setDeleteNoteListener(this);
        noteRecyclerView.setAdapter(noteAdapter);
        noteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // Load existing notes on app startup
        loadSavedNotes();

        // Set up click listener for the save button
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the entered note
                addNewNoteToStorage(String.valueOf(noteEditText.getText()));

                // Show a Snackbar indicating successful note saving
                Snackbar.make(mainLayout, "Note saved successfully!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    // Method to save a note to SharedPreferences and update the UI
    private void addNewNoteToStorage(String note) {
        if (!note.isEmpty()) {
            // Get SharedPreferences editor
            SharedPreferences.Editor editor = notePreferences.edit();

            // Save the note with a key-value pair
            editor.putString(note, note);

            // Apply the changes
            editor.apply();

            // Update the local notes ArrayList and notify the adapter
            noteList.add(note);
            noteAdapter.notifyDataSetChanged();

            // Clear the edit text for a new note
            noteEditText.getText().clear();
        }
    }

    // Method to load existing notes from SharedPreferences
    private void loadSavedNotes() {
        // Clear the existing notes ArrayList
        noteList.clear();

        // Get all key-value pairs from SharedPreferences
        Map<String, ?> allNotes = notePreferences.getAll();

        // Iterate through the entries and add notes to the local ArrayList
        for (Map.Entry<String, ?> entry : allNotes.entrySet()) {
            noteList.add(entry.getValue().toString());
        }

        // Notify the adapter about the data change
        noteAdapter.notifyDataSetChanged();
    }

    // Override method from NotesAdapter.DeleteNoteListener to handle note deletion
    @Override
    public void deleteNote(String note) {
        // Get SharedPreferences editor
        SharedPreferences.Editor editor = notePreferences.edit();

        // Remove the note using its key
        editor.remove(note);

        // Apply the changes
        editor.apply();

        // Reload the notes to update the UI
        loadSavedNotes();
    }
}
