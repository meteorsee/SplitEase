package com.example.individualassignment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

public class show_saved_names extends AppCompatActivity {

    private ListView lvSavedNames;
    private ArrayAdapter<String> namesAdapter;
    private List<String> savedNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_names);

        Log.d("show_saved_names", "Activity created");

        // Find ListView in layout
        lvSavedNames = findViewById(R.id.lvSavedNames);

        // Load saved names from SharedPreferences and sort them alphabetically
        savedNames = loadEnteredNames();
        Collections.sort(savedNames); // Sort the list alphabetically

        Log.d("show_saved_names", "Loaded " + savedNames.size() + " saved names");  //log all names when the names start to load

        // Create an ArrayAdapter for ListView
        namesAdapter = new ArrayAdapter<>(this, R.layout.saved_name_custom, savedNames);
        lvSavedNames.setAdapter(namesAdapter);

        lvSavedNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Handle item click in Edit mode (show delete confirmation)
                    String nameToRemove = savedNames.get(position);
                    showDeleteConfirmation(nameToRemove);
            }
        });

        // Customize the back button in the action bar
        customizeBackButton();
    }

    // Show a confirmation dialog for deleting a name
    private void showDeleteConfirmation(final String nameToRemove) {
        Log.d("show_saved_names", "Showing delete confirmation dialog"); //log names that wants to delete

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.alert_dialog_custom, null);
        builder.setView(customView);

        // Customize dialog title
        TextView dialogTitle = customView.findViewById(R.id.dialogTitle);
        dialogTitle.setTypeface(Typeface.DEFAULT_BOLD); // Set a custom font if desired
        dialogTitle.setTextColor(Color.BLACK); // Set the font color

        // Customize dialog message
        TextView dialogMessage = customView.findViewById(R.id.dialogMessage);
        dialogMessage.setTypeface(Typeface.DEFAULT); // Set a custom font if desired
        dialogMessage.setTextColor(Color.GRAY); // Set the font color

        // Set positive and negative buttons for the dialog
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeName(nameToRemove);
            }
        })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Remove a name from the list and update SharedPreferences
    private void removeName(String name) {
        Log.d("show_saved_names", "Removing name: " + name); //log the deleted names

        savedNames.remove(name);
        saveEnteredNames(savedNames);
        namesAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Name removed: " + name, Toast.LENGTH_SHORT).show();
    }

    // Load the list of names from SharedPreferences
    private void saveEnteredNames(List<String> names) {
        SharedPreferences sharedPreferences = getSharedPreferences("NamePreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> nameSet = new HashSet<>(names);
        editor.putStringSet("EnteredNames", nameSet);
        editor.apply();
    }

    private List<String> loadEnteredNames() {
        SharedPreferences sharedPreferences = getSharedPreferences("NamePreferences", MODE_PRIVATE);
        Set<String> nameSet = sharedPreferences.getStringSet("EnteredNames", new HashSet<>());
        return new ArrayList<>(nameSet);
    }

    // Customize the back button in the action bar
    private void customizeBackButton() {
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Customize the back button
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

            // showing the back button in the action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //Create an options menu at the top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_custom,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item1){

        int id = item1.getItemId();

        if(id == R.id.exit){
            finishAffinity(); // Finish the current activity and all activities below it
            System.exit(0); // Terminate the app process

        } else if (id == android.R.id.home) { // Handle back button press
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item1);
    }
}
