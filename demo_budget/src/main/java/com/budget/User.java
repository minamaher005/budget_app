package com.budget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.budget.budget_management.Budget;
import com.budget.budget_management.BudgetFactory;
import com.budget.budget_management.BudgetFactory.BudgetType;
import com.budget.budget_management.Expense;
import com.budget.budget_management.IBudgetManager;
import com.budget.income_management.IIncomeSource;
import com.budget.income_management.Income;
import com.budget.income_management.IncomeFactory;
import com.budget.income_management.IncomeFactory.IncomeType;

public class User {
    private static final List<User> users = new ArrayList<>();
    private final String name;
    private String email;
    private String password;
    private List<Income> incomes;
    private float totalIncome;
    private IncomeFactory incomeFactory;
    private BudgetFactory budgetFactory;
    private List<IBudgetManager> budgets;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.incomes = new ArrayList<>();
        this.totalIncome = 0;
        this.incomeFactory = new IncomeFactory();
        this.budgetFactory = new BudgetFactory();
        this.budgets = new ArrayList<>();
    }

    public List<Budget> getBudgets() {
        List<Budget> userBudgets = new ArrayList<>();
        for (IBudgetManager budget : budgets) {
            if (budget instanceof Budget) {
                userBudgets.add((Budget) budget);
            }
        }
        return userBudgets;
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

    public void addIncome(Income income) {
        incomes.add(income);
        totalIncome += income.getAmount();
        income.recordIncome();
    }

    public void createIncome(IncomeType type, float amount, Date date, String name) {
        try {
            IIncomeSource incomeSource = IncomeFactory.createIncome(type, amount, date, name);
            if (incomeSource instanceof Income) {
                Income income = (Income) incomeSource;
                addIncome(income);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create income: " + e.getMessage());
        }
    }

    public void createBudget(BudgetType type, String category, float limit) {
        if (limit > totalIncome) {
            System.out.println("Insufficient funds to create budget");
            return;
        }
        IBudgetManager budget = BudgetFactory.createBudget(type);
        budget.setBudgetLimit(limit);
        if (budget instanceof Budget) {
            ((Budget) budget).setCategory(category);
        }
        budgets.add(budget);
        totalIncome -= limit;
        System.out.println("Budget created: " + category + ", limit: " + limit);
    }

    public void addExpenseToBudget(String budgetCategory, float amount, String expenseCategory, Date date) {
        for (IBudgetManager budget : budgets) {
            if (budget instanceof Budget && ((Budget)budget).getCategory().equals(budgetCategory)) {
                budget.addExpense(amount, expenseCategory, date);
                if (budget instanceof Budget && ((Budget)budget).isBudgetExceeded()) {
                    System.out.println("Warning: Budget " + budgetCategory + " exceeded!");
                }
                return;
            }
        }
        System.out.println("Budget not found: " + budgetCategory);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        for (IBudgetManager budget : budgets) {
            if (budget instanceof Budget) {
                allExpenses.addAll(((Budget)budget).getExpenses());
            }
        }
        return allExpenses;
    }

    public float getTotalSpending() {
        float total = 0;
        for (IBudgetManager budget : budgets) {
            if (budget instanceof Budget) {
                total += ((Budget)budget).getTotalExpenses();
            }
        }
        return total;
    }

    public float getTotalIncome() {
        return totalIncome;
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