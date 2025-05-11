package com.budget;

import java.util.ArrayList;
import java.util.List;

import com.budget.budget_management.BudgetManager;

public class User {
    private static final List<User> users = new ArrayList<>();
    private final String name;
    private String email;
    private String password;
    private BudgetManager budgetManager;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.budgetManager = new BudgetManager();
    }

    public boolean signup() {
        for (User user : users) {
            if (user.name.equals(this.name)) {
                System.out.println("Username already exists");
                return false;
            }
        }
        users.add(this);
        System.out.println("User signed up: " + name);
        return true;
    }

    public static User login(String username, String password) {
        for (User user : users) {
            if (user.name.equals(username) && user.password.equals(password)) {
                System.out.println("Login successful for " + username);
                return user;
            }
        }
        System.out.println("Invalid username or password");
        return null;
    }

    public BudgetManager getBudgetManager() {
        return budgetManager;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void recoverAccount() {
        System.out.println("Account recovery initiated for " + email);
    }


}