package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Define a tag for logging
    private Button btnEqualScreen, btnCustomScreen, btnSavedNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Activity started");

        //Buttons for equal & custom breakdown
        btnEqualScreen = findViewById(R.id.btnEqualBreakdownPage);
        btnCustomScreen = findViewById(R.id.btnCustomBreakdownPage);
        btnSavedNames = findViewById(R.id.SavedNames);

        //Equal breakdown screen
        btnEqualScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Equal breakdown button clicked");
                // Create an Intent to start the next activity
                Intent intent = new Intent(MainActivity.this, EqualBreakdown.class);
                startActivity(intent);
            }
        });

        //Custom breakdown screen
        btnCustomScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Log.d(TAG, "onClick: Custom breakdown button clicked");
                // Create an Intent to start the next activity
                Intent intent_custom = new Intent(MainActivity.this, CustomBreakdown.class);
                startActivity(intent_custom);
            }
        });

        //Saved names screen
        btnSavedNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Log.d(TAG, "onClick: Saved names button clicked");
                // Create an Intent to start the next activity
                Intent intent_names = new Intent(MainActivity.this, show_saved_names.class);
                startActivity(intent_names);
            }
        });
    }
    //Top right corner menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_custom,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item1){

        int id = item1.getItemId();

        if(id == R.id.exit) {
            finishAffinity(); // Finish the current activity and all activities below it
            System.exit(0); // Terminate the app process
        }

        return super.onOptionsItemSelected(item1);
    }
}
