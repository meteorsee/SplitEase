package com.example.individualassignment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CustomBreakdown extends AppCompatActivity {
    private static final String TAG = "CustomBreakdownScreen"; // Define a tag for logging

    private Button btnByPercentage;
    private Button btnByAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_breakdown);

        Log.d(TAG, "onCreate: Activity started");

        //Buttons for percentage and amount breakdown
        btnByPercentage = findViewById(R.id.btnByPercentage);
        btnByAmount = findViewById(R.id.btnByAmount);

        //Breakdown by percentage
        btnByPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: By Percentage button clicked");
                // Create an Intent to start the next activity
                Intent intent = new Intent(CustomBreakdown.this, ByPercentage.class);
                startActivity(intent);
            }
        });

        //Breakdown by amount
        btnByAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Log.d(TAG, "onClick: By Amount button clicked");
                // Create an Intent to start the next activity
                Intent intent_custom = new Intent(CustomBreakdown.this, ByAmount.class);
                startActivity(intent_custom);
            }
        });


        customizeBackButton();
    }

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

    //Top right corner menu
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