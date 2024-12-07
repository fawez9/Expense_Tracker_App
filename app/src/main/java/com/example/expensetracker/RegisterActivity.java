package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        dbHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(RegisterActivity.this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(name, email, password);
            }
        });
    }

    private void registerUser(String name, String email, String password) {
        boolean isRegistered = dbHelper.registerUser(name, email, password);

        if (isRegistered) {
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            // Redirect to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email might already be registered.", Toast.LENGTH_SHORT).show();
        }
    }
}
