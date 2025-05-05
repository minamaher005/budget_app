package com.budget;

import java.util.Date;
import java.util.Scanner;

import com.budget.income_management.IIncomeSource;
import com.budget.income_management.Income;
import com.budget.income_management.IncomeFactory;

public class Main {
    private static User currentUser;
    private static IncomeFactory incomeFactory;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        incomeFactory = new IncomeFactory();
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Budget App - Login Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Sign up");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                signup();
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== Budget App - Main Menu ===");
        System.out.println("Welcome, " + currentUser.getName());
        System.out.println("1. Add Income");
        System.out.println("2. Create Budget");
        System.out.println("3. Add Expense");
        System.out.println("4. View Total Income");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addIncome();
                break;
            case 2:
                createBudget();
                break;
            case 3:
                addExpense();
                break;
            case 4:
                viewTotalIncome();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = User.login(username, password);
        if (currentUser == null) {
            System.out.println("Login failed. Please try again.");
        }
    }

    private static void signup() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = new User(name, email, password);
        if (currentUser.signup()) {
            System.out.println("Signup successful!");
        } else {
            System.out.println("Signup failed. User might already exist.");
            currentUser = null;
        }
    }

    private static void addIncome() {
        System.out.println("\n=== Add Income ===");
        System.out.println("Income types: salary, investment, other");
        System.out.print("Enter income type: ");
        String type = scanner.nextLine();
        
        System.out.print("Enter amount: ");
        float amount = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter income name: ");
        String name = scanner.nextLine();

        IIncomeSource incomeSource = incomeFactory.createIncome(type, amount, new Date(), name);
        if (incomeSource instanceof Income) {
            currentUser.addIncome((Income) incomeSource);
            System.out.println("Income added successfully!");
        }
    }

    private static void createBudget() {
        System.out.println("\n=== Create Budget ===");
        System.out.print("Enter budget type (monthly/weekly): ");
        String type = scanner.nextLine();
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        
        System.out.print("Enter limit: ");
        float limit = scanner.nextFloat();
        scanner.nextLine(); // Consume newline

        currentUser.createBudget(type, category, limit);
    }

    private static void addExpense() {
        System.out.println("\n=== Add Expense ===");
        System.out.print("Enter budget category: ");
        String budgetCategory = scanner.nextLine();
        
        System.out.print("Enter expense category: ");
        String expenseCategory = scanner.nextLine();
        
        System.out.print("Enter amount: ");
        float amount = scanner.nextFloat();
        scanner.nextLine(); // Consume newline

        currentUser.addExpenseToBudget(budgetCategory, amount, expenseCategory, new Date());
    }

    private static void viewTotalIncome() {
        System.out.println("\nTotal Income: $" + currentUser.getTotalIncome());
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }
}