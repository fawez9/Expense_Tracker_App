package com.example.expensetracker;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private MutableLiveData<List<Expense>> allExpenses;

    public ExpenseRepository() {
        allExpenses = new MutableLiveData<>();
        loadExpenses(); // Simulate loading data from database
    }

    private void loadExpenses() {
        // Simulate getting expenses from a database or API
        List<Expense> expenses = new ArrayList<>();

        allExpenses.setValue(expenses); // Set the data in LiveData
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;  // Return LiveData object
    }

    // Method to insert an Expense (you can implement the actual database insertion here)
    public void insert(Expense expense) {
        // Add the expense to the list (simulate database insert)
        List<Expense> expenses = allExpenses.getValue();
        if (expenses != null) {
            expenses.add(expense);
            allExpenses.setValue(expenses);  // Update LiveData after insertion
        }
    }
}
