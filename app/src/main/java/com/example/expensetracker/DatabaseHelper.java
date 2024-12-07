package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USERS_TABLE = "users";
    private static final String EXPENSES_TABLE = "expenses";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)");
        db.execSQL("CREATE TABLE " + EXPENSES_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, name TEXT, amount REAL, FOREIGN KEY(userId) REFERENCES users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSES_TABLE);
        onCreate(db);
    }

    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert(USERS_TABLE, null, values);
        return result != -1; // Registration success
    }

    public Cursor authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE email = ? AND password = ?", new String[]{email, password});
    }

    // Method to get the user ID by email
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + USERS_TABLE + " WHERE email = ?", new String[]{email});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }

    public void insertExpense(int userId, String name, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("name", name);
        values.put("amount", amount);
        db.insert(EXPENSES_TABLE, null, values);
    }

    public Cursor getExpensesByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + EXPENSES_TABLE + " WHERE userId = ?", new String[]{String.valueOf(userId)});
    }

    public void updateExpense(int expenseId, String name, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("amount", amount);
        db.update(EXPENSES_TABLE, values, "id = ?", new String[]{String.valueOf(expenseId)});
    }

    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXPENSES_TABLE, "id = ?", new String[]{String.valueOf(expenseId)});
    }
}