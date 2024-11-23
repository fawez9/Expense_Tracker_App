package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvExpenses;
    private TextView tvTotalExpenses;
    private Button btnAddExpense;
    private ArrayList<Expense> expenseList;
    private ArrayAdapter<String> adapter;

    private double totalExpenses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvExpenses = findViewById(R.id.lvExpenses);
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        expenseList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lvExpenses.setAdapter(adapter);

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            double amount = data.getDoubleExtra("amount", 0);

            expenseList.add(new Expense(name, amount));
            totalExpenses += amount;

            adapter.add(name + " - $" + amount);
            tvTotalExpenses.setText("Total: $" + totalExpenses);
        }
    }
}