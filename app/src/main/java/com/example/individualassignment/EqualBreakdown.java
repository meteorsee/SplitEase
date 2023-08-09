package com.example.individualassignment;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EqualBreakdown extends AppCompatActivity {

    private static final String TAG = "EqualBreakdown"; // Define a tag for logging

    private EditText etTotalBill, etNumberOfPeople;
    private Button btnEqualBreakdown;
    private TextView tvBreakdownResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_breakdown);

        etTotalBill = findViewById(R.id.etTotalBill);
        etNumberOfPeople = findViewById(R.id.etNumberOfPeople);
        btnEqualBreakdown = findViewById(R.id.btnEqualBreakdown);
        tvBreakdownResult = findViewById(R.id.tvBreakdownResult);

        Log.d(TAG, "onCreate: Activity started"); // Log the activity start

        btnEqualBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateEqualBreakdown();
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

    private void calculateEqualBreakdown() {
        Log.d(TAG, "calculateEqualBreakdown: Calculating breakdown");

        String totalBillString = etTotalBill.getText().toString();
        String numberOfPeopleString = etNumberOfPeople.getText().toString();

        if (totalBillString.isEmpty() || numberOfPeopleString.isEmpty()) {
            Toast.makeText(this, "Please enter total bill and number of people.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalBill = Double.parseDouble(totalBillString);
        int numberOfPeople = Integer.parseInt(numberOfPeopleString);

        if (numberOfPeople == 0) {
            Toast.makeText(this, "Number of people cannot be zero.", Toast.LENGTH_SHORT).show();
            return;
        }

        double individualAmount = totalBill / numberOfPeople;

        // Display breakdown result
        StringBuilder breakdownResult = new StringBuilder();
        breakdownResult.append("Equal Breakdown:\n");

        for (int i = 1; i <= numberOfPeople; i++) {
            breakdownResult.append("Person ").append(i).append(": $").append(String.format("%.2f", individualAmount)).append("\n");
        }

        tvBreakdownResult.setText(breakdownResult.toString());
        tvBreakdownResult.setVisibility(View.GONE);
        showBreakdownResultDialog(breakdownResult.toString());
    }

    private void showBreakdownResultDialog(String result) {
        Log.d(TAG, "showBreakdownResultDialog: Showing breakdown result dialog");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.equal_breakdown_dialog);
        TextView dialogBreakdownResult = dialog.findViewById(R.id.dialogBreakdownResult);
        Button btnShare = dialog.findViewById(R.id.btnShare);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        dialogBreakdownResult.setText(result);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareResultFromDialog(result);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tvBreakdownResult.setVisibility(View.VISIBLE);
            }
        });

        dialog.show();
    }


    private void shareResultFromDialog(String breakdownResult) {
        Log.d(TAG, "shareResultFromDialog: Sharing breakdown result");

        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Create a sharing intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Expense Breakdown");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Date: " + currentDate + "\n" + breakdownResult);

        // Check if WhatsApp is installed
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            // Create a chooser dialog to allow users to choose a sharing app
            Intent chooserIntent = Intent.createChooser(shareIntent, "Share breakdown result");
            startActivity(chooserIntent);
        } else {
            Toast.makeText(this, "No app available for sharing.", Toast.LENGTH_SHORT).show();
        }
    }

    //Top right corner menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item1){

        int id = item1.getItemId();

        if(id == R.id.exit){
            finish();
        } else if(id == R.id.share_result){
            String breakdownResult = tvBreakdownResult.getText().toString();
            shareResultFromDialog(breakdownResult);
        } else if (id == android.R.id.home) { // Handle back button press
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item1);
    }
}