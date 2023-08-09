package com.example.individualassignment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ByAmount extends AppCompatActivity {

    private static final String TAG = "ByAmount"; // Define a tag for logging

    private EditText etTotalBill, etNumberOfPeople;
    private Button btnCalculate, btnProceed, btnClear;
    private TextView tvBreakdownResult;
    private LinearLayout llContainer;
    private AutoCompleteTextView nameAutoComplete;
    private ArrayAdapter<String> nameAdapter;
    private CheckBox chkStoreNames;
    private List<String> enteredNames = new ArrayList<>();  //List to store entered names
    private boolean proceedClicked = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_amount);

        etTotalBill = findViewById(R.id.etTotalBill);
        etNumberOfPeople = findViewById(R.id.etNumberOfPeople);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvBreakdownResult = findViewById(R.id.tvBreakdownResult);
        btnProceed = findViewById(R.id.btnProceed); // Update button reference
        llContainer = findViewById(R.id.llContainer);
        btnClear = findViewById(R.id.btnClear);
        chkStoreNames = findViewById(R.id.chkStoreNames);

        Log.d(TAG, "onCreate: Activity started"); // Log the activity start

        // Initialize the AutoCompleteTextView and its adapter
        nameAutoComplete = new AutoCompleteTextView(this);
        nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        nameAutoComplete.setAdapter(nameAdapter);

        //Proceed Button
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Proceed button clicked");
                proceedClicked = true;
                showAmountList();
            }
        });

        //Calculation Button
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Calculate button clicked");
                if (!proceedClicked) {
                    Toast.makeText(ByAmount.this, "Please click the Proceed button first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                calculateBreakdown();
            }
        });

        //Clear Button
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clear button clicked");
                proceedClicked = false;
                // Clear the calculation, result all things
                clearCalculation();
            }
        });

        // Load previously entered names from SharedPreferences
        enteredNames = loadEnteredNames();
        updateNameAdapter(enteredNames);

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

    private void showAmountList() {
        Log.d(TAG, "showAmountList: Displaying amount list");
        String totalBillString = etTotalBill.getText().toString();
        String numberOfPeopleString = etNumberOfPeople.getText().toString();

        if (totalBillString.isEmpty() || numberOfPeopleString.isEmpty()) {
            Toast.makeText(this, "Please enter total bill and number of people.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalBill = Double.parseDouble(totalBillString);
        int numberOfPeople = Integer.parseInt(numberOfPeopleString);

        if (numberOfPeople <= 0) {
            Toast.makeText(this, "Number of people must be greater than zero.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove the previous views before displaying the amount list
        llContainer.removeAllViews();

        // Add dynamically generated EditText fields for name and amount
        for (int i = 0; i < numberOfPeople; i++) {
            LinearLayout personLayout = new LinearLayout(this);
            personLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            personLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Names
            AutoCompleteTextView nameAutoCompleteTextView = new AutoCompleteTextView(this);
            nameAutoCompleteTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            nameAutoCompleteTextView.setHint("Name for Person " + (i + 1));
            nameAutoCompleteTextView.setAdapter(nameAdapter); // Set the adapter

            personLayout.addView(nameAutoCompleteTextView);

            //Amount
            EditText amountEditText = new EditText(this);
            amountEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            amountEditText.setHint("Amount for Person " + (i + 1));
            amountEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            personLayout.addView(amountEditText);
            llContainer.addView(personLayout);
        }
    }


    private void calculateBreakdown() {
        Log.d(TAG, "calculateBreakdown: Calculating breakdown");
        double totalBillInteger = Double.parseDouble(etTotalBill.getText().toString());
        double numberOfPeople = Integer.parseInt(etNumberOfPeople.getText().toString());
        List<Double> amounts = new ArrayList<>();   //list of amounts
        List<String> names = new ArrayList<>();     //list of names
        double totalAmount = 0;

        // Get the amount values entered by the user and calculate the total amount
        for (int i = 0; i < numberOfPeople; i++) {
            LinearLayout personLayout = (LinearLayout) llContainer.getChildAt(i);
            // AutoCompleteTextView for name
            AutoCompleteTextView etName = (AutoCompleteTextView) personLayout.getChildAt(0);
            String name = etName.getText().toString();

            // EditText for amount
            EditText etAmount = (EditText) personLayout.getChildAt(1);
            String amountString = etAmount.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter name for Person " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            if (amountString.isEmpty()) {
                Toast.makeText(this, "Please enter amount for Person " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountString);
            amounts.add(amount);
            totalAmount += amount;
            names.add(name);
        }

        if (totalAmount != totalBillInteger) {
            Toast.makeText(this, "Total amount must add up equal to the entered amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save entered names to SharedPreferences if the checkbox is checked
        if (chkStoreNames.isChecked()) {
            Toast.makeText(this, "Name has stored", Toast.LENGTH_SHORT).show();
            enteredNames.addAll(names);
            saveEnteredNames(enteredNames);
        }

        // Calculate individual breakdown amounts based on entered amount
        double totalBill = Double.parseDouble(etTotalBill.getText().toString());
        StringBuilder breakdownResult = new StringBuilder();
        breakdownResult.append("Breakdown by Amount:\n");

        for (int i = 0; i < numberOfPeople; i++) {
            double individualAmount = amounts.get(i);
            breakdownResult.append(names.get(i)).append(": RM").append(String.format("%.2f", individualAmount)).append("\n");
        }

        // Display the breakdown result
        tvBreakdownResult.setText(breakdownResult.toString());
        tvBreakdownResult.setVisibility(View.GONE);
        showBreakdownResultDialog(breakdownResult.toString());
    }


    // Clear the calculation and show the "Proceed" button
    private void clearCalculation() {
        Log.d(TAG, "clearCalculation: Clearing calculation");
        etTotalBill.setText("");
        etNumberOfPeople.setText("");
        tvBreakdownResult.setText("");
        proceedClicked = false; // Reset the proceedClicked flag

        llContainer.removeAllViews();
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

    // Share the breakdown result
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
    // Update the name adapter with new suggestions
    private void updateNameAdapter(List<String> names) {
        Log.d(TAG, "updateNameAdapter: Updating name adapter");
        nameAdapter.clear();
        nameAdapter.addAll(names);
        nameAdapter.notifyDataSetChanged();
    }

    // Save entered names to SharedPreferences
    private void saveEnteredNames(List<String> names) {
        SharedPreferences sharedPreferences = getSharedPreferences("NamePreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> nameSet = new HashSet<>(names);
        editor.putStringSet("EnteredNames", nameSet);
        editor.apply();
    }

    // Load entered names from SharedPreferences
    private List<String> loadEnteredNames() {
        SharedPreferences sharedPreferences = getSharedPreferences("NamePreferences", MODE_PRIVATE);
        Set<String> nameSet = sharedPreferences.getStringSet("EnteredNames", new HashSet<>());
        return new ArrayList<>(nameSet);
    }

    // Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
