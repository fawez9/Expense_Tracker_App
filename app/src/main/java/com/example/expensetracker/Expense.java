package com.example.expensetracker;

public class Expense {
    private int id; // Unique database ID for the expense
    private String name; // Name/description of the expense
    private double amount; // Monetary amount of the expense
    private int userId; // User ID associated with this expense
    private String userEmail; // Optional email for reference
    private String date;

    // Constructor with userId
    public Expense(String name, double amount, int userId) {
        this.name = name;
        this.amount = amount;
        this.userId = userId;
    }

    // Constructor with userId and userEmail
    public Expense(String name, double amount, int userId, String userEmail) {
        this.name = name;
        this.amount = amount;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    // Constructor with full details including id
    public Expense(int id, String name, double amount, int userId, String userEmail) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    // Getters and setter

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // Comprehensive getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Validation method to ensure expense data is valid
    public boolean isValid() {
        return name != null && !name.trim().isEmpty()
                && amount > 0
                && userId > 0;
    }

    // Create a copy of the current expense
    public Expense copy() {
        return new Expense(this.id, this.name, this.amount, this.userId, this.date);
    }

    // Custom toString for easy display
    @Override
    public String toString() {
        return String.format("%s - $%.2f", name, amount);
    }

    // Equals method for comparing expenses
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Expense expense = (Expense) obj;
        return id == expense.id
                && Double.compare(expense.amount, amount) == 0
                && userId == expense.userId
                && name.equals(expense.name);
    }

    // Hashcode method to support use in collections
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.hashCode();
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + userId;
        return result;
    }
}