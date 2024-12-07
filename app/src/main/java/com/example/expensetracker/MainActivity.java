package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvExpenses;
    private TextView tvTotalExpenses;
    private Button btnAddExpense, btnLogout;

    private ArrayList<Expense> expenseList;
    private ArrayAdapter<String> adapter;
    private double totalExpenses = 0;

    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private int userId;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        lvExpenses = findViewById(R.id.lvExpenses);
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnLogout = findViewById(R.id.btnLogout);

        // Initialize expense list and adapter
        expenseList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lvExpenses.setAdapter(adapter);

        // Load user data from SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "");
        userId = dbHelper.getUserId(userEmail);

        loadExpensesFromDatabase();

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(intent, 1);
        });

        lvExpenses.setOnItemClickListener((parent, view, position, id) -> showExpenseOptionsDialog(position));

        btnLogout.setOnClickListener(v -> logout());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            double amount = data.getDoubleExtra("amount", 0);

            // Create expense with userId
            Expense expense = new Expense(name, amount, userId, userEmail);
            expenseList.add(expense);

            // Save expense to database with userId
            dbHelper.insertExpense(userId, name, amount);

            totalExpenses += amount;
            updateExpenseList();
        }
    }

    private void showExpenseOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        builder.setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                editExpense(position);
            } else {
                deleteExpense(position);
            }
        });
        builder.show();
    }

    private void loadExpensesFromDatabase() {
        // Clear existing list
        expenseList.clear();
        totalExpenses = 0;

        // Retrieve expenses for the specific user
        Cursor cursor = dbHelper.getExpensesByUserId(userId);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            int expenseId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

            // Create expense with all details
            Expense expense = new Expense(expenseId, name, amount, userId, userEmail);
            expenseList.add(expense);
            totalExpenses += amount;
        }
        cursor.close();

        updateExpenseList();
    }

    private void editExpense(int position) {
        Expense expense = expenseList.get(position);

        // Create a dialog for editing the expense
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Expense");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_expense, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etEditExpenseName);
        EditText etAmount = dialogView.findViewById(R.id.etEditExpenseAmount);

        etName.setText(expense.getName());
        etAmount.setText(String.valueOf(expense.getAmount()));

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = etName.getText().toString().trim();
            double newAmount = Double.parseDouble(etAmount.getText().toString().trim());

            totalExpenses -= expense.getAmount(); // Subtract old amount
            totalExpenses += newAmount; // Add new amount

            expense.setName(newName);
            expense.setAmount(newAmount);

            updateExpenseInDatabase(expense);
            updateExpenseList();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteExpense(int position) {
        Expense expense = expenseList.remove(position);
        totalExpenses -= expense.getAmount();
        deleteExpenseFromDatabase(expense);
        updateExpenseList();
    }

    private void updateExpenseList() {
        ArrayList<String> expenseDisplayList = new ArrayList<>();
        for (Expense expense : expenseList) {
            expenseDisplayList.add(expense.getName() + " - $" + expense.getAmount());
        }
        adapter.clear();
        adapter.addAll(expenseDisplayList);
        adapter.notifyDataSetChanged();

        tvTotalExpenses.setText("Total Expenses: $" + String.format("%.2f", totalExpenses));
    }

    private void updateExpenseInDatabase(Expense expense) {
        // Update expense in database
        dbHelper.updateExpense(expense.getId(), expense.getName(), expense.getAmount());
    }

    private void deleteExpenseFromDatabase(Expense expense) {
        // Delete expense from database
        dbHelper.deleteExpense(expense.getId());
    }

    private void logout() {
        // Clear the logged-in state from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // This will remove all stored preferences
        editor.apply();

        // Redirect to LoginActivity and finish MainActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}