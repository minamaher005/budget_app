package com.budget;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.budget.budget_management.BudgetFactory.BudgetType;
import com.budget.budget_management.Expense;
import com.budget.income_management.IncomeFactory.IncomeType;

public class Main {
    private static User currentUser;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
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
        if (currentUser != null) {
            
            for (com.budget.budget_management.Budget budget : currentUser.getBudgetManager().getBudgets()) {

                budget.validateReminders();
            }
        }
        System.out.println("\n=== Budget App - Main Menu ===");
        System.out.println("Welcome, " + currentUser.getName());
        System.out.println("1. Add Income");
        System.out.println("2. Create Budget");
        System.out.println("3. Add Expense");
        System.out.println("4. View Total Income");
        System.out.println("5. View All Expenses");
        System.out.println("6. View Total Spending");
        System.out.println("7. Manage Reminders");
        System.out.println("8. View All Budgets");
        System.out.println("9. Logout");
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
                showAllExpenses();
                break;
            case 6:
                viewTotalSpending();
                break;
            case 7:
                manageReminders();
                break;
            case 8:
                viewAllBudgets();
                break;
            case 9:
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
        System.out.println("Income types: SALARY, INVESTMENT, OTHER");
        System.out.print("Enter income type: ");
        String typeStr = scanner.nextLine().toUpperCase();
        IncomeType type;
        try {
            type = IncomeType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid income type. Please use SALARY, INVESTMENT, or OTHER.");
            return;
        }
        
        System.out.print("Enter amount: ");
        float amount = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter income name: ");
        String name = scanner.nextLine();

        currentUser.getBudgetManager().createIncome(type, amount, new Date(), name);
        System.out.println("Income added successfully!");
    }

    private static void createBudget() {
        System.out.println("\n=== Create Budget ===");
        System.out.println("Budget types: PERSONAL, BUSINESS");
        System.out.print("Enter budget type: ");
        String typeStr = scanner.nextLine().toUpperCase();
        BudgetType type;
        try {
            type = BudgetType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid budget type. Please use PERSONAL or BUSINESS.");
            return;
        }
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        
        System.out.print("Enter limit: ");
        float limit = scanner.nextFloat();
        scanner.nextLine(); // Consume newline

        currentUser.getBudgetManager().createBudget(type, category, limit);
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

        currentUser.getBudgetManager().addExpenseToBudget(budgetCategory, amount, expenseCategory, new Date());
    }

    private static void viewTotalIncome() {
        System.out.println("\nTotal Income: $" + currentUser.getBudgetManager().getTotalIncome());
    }

    private static void showAllExpenses() {
        List<Expense> expenses = currentUser.getBudgetManager().getAllExpenses();
        System.out.println("\n=== All Expenses ===");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    private static void viewTotalSpending() {
        System.out.println("\nTotal Spending: $" + currentUser.getBudgetManager().getTotalSpending());
    }

    private static void manageReminders() {
        System.out.println("\n=== Manage Reminders ===");
        System.out.println("1. Add Reminder");
        System.out.println("2. List Reminders");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addReminder();
                break;
            case 2:
                listReminders();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void addReminder() {
        System.out.print("Enter reminder description: ");
        String description = scanner.nextLine();

        System.out.print("Enter reminder date and time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date dateTime;
        try {
            dateTime = sdf.parse(dateTimeStr);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm.");
            return;
        }

        com.budget.budget_management.Reminder reminder = new com.budget.budget_management.Reminder(description, dateTime);

        // Add reminder to all budgets
        for (com.budget.budget_management.Budget budget : currentUser.getBudgetManager().getBudgets()) {
            budget.addReminder(reminder);
        }

        System.out.println("Reminder added successfully!");
    }

    private static void listReminders() {
        System.out.println("\n=== List of Reminders ===");
        boolean hasReminders = false;

        for (com.budget.budget_management.Budget budget : currentUser.getBudgetManager().getBudgets()) {
            java.util.List<com.budget.budget_management.Reminder> reminders = budget.getReminders();
            if (!reminders.isEmpty()) {
                hasReminders = true;
                System.out.println("\nCategory: " + budget.getCategory());
                System.out.println("------------------------------");
                for (com.budget.budget_management.Reminder reminder : reminders) {
                    System.out.println("• Description: " + reminder.getDescription());
                    System.out.println("  Date & Time: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(reminder.getDateTime()));
                    System.out.println();
                }
            }
        }

        if (!hasReminders) {
            System.out.println("No reminders found.");
        }
    }

    private static void viewAllBudgets() {
        System.out.println("\n=== All Budgets ===");
        List<com.budget.budget_management.Budget> budgets = currentUser.getBudgetManager().getBudgets();
        if (budgets.isEmpty()) {
            System.out.println("No budgets found.");
            return;
        }
        for (com.budget.budget_management.Budget budget : budgets) {
            System.out.println("Category: " + budget.getCategory() + 
                               ", Limit: $" + budget.getBudgetLimit() + 
                               ", Total Expenses: $" + budget.getTotalExpenses());
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
}