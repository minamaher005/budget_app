package com.budget.budget_management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.budget.budget_management.BudgetFactory.BudgetType;
import com.budget.income_management.IIncomeSource;
import com.budget.income_management.Income;
import com.budget.income_management.IncomeFactory;
import com.budget.income_management.IncomeFactory.IncomeType;

public class BudgetManager implements IBudgetManager {
    private final List<Budget> budgets;
    private float allocatedIncome;

    public BudgetManager() {
        this.budgets = new ArrayList<>();
        this.allocatedIncome = 0;
    }

    public void createIncome(IncomeType type, float amount, Date date, String name) {
        try {
            IIncomeSource incomeSource = IncomeFactory.createIncome(type, amount, date, name);
            if (incomeSource instanceof Income) {
                Income.addIncome((Income) incomeSource);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create income: " + e.getMessage());
        }
    }

    public void createBudget(BudgetType type, String category, float limit) {
        float availableIncome = Income.getTotalIncome() - allocatedIncome;
        if (limit > availableIncome) {
            System.out.println("Insufficient funds to create budget. Available: $" + availableIncome);
            return;
        }
        
        Budget budget = BudgetFactory.createBudget(type);
        budget.setBudgetLimit(limit);
        budget.setCategory(category);
        budgets.add(budget);
        allocatedIncome += limit;
        
        System.out.println("Budget created: " + category + ", limit: " + limit);
        System.out.println("Remaining available income: $" + (Income.getTotalIncome() - allocatedIncome));
    }

    @Override
    public void setBudgetLimit(float amount) {
        if (!budgets.isEmpty()) {
            budgets.get(budgets.size() - 1).setBudgetLimit(amount);
        }
    }

    @Override
    public void addExpense(float amount, String category, Date date) {
        if (!budgets.isEmpty()) {
            budgets.get(budgets.size() - 1).addExpense(amount, category, date);
        }
    }

    @Override
    public void editExpense(int expenseId, float amount, String category, Date date) {
        for (Budget budget : budgets) {
            budget.editExpense(expenseId, amount, category, date);
        }
    }

    @Override
    public void deleteExpense(int expenseId) {
        for (Budget budget : budgets) {
            budget.deleteExpense(expenseId);
        }
    }

    @Override
    public void setExpenseRecurring(int expenseId, boolean isRecurring) {
        for (Budget budget : budgets) {
            budget.setExpenseRecurring(expenseId, isRecurring);
        }
    }

    @Override
    public List<String> getRecommendations() {
        List<String> recommendations = new ArrayList<>();
        for (Budget budget : budgets) {
            recommendations.addAll(budget.getRecommendations());
        }
        return recommendations;
    }

    public void addExpenseToBudget(String budgetCategory, float amount, String expenseCategory, Date date) {
        for (Budget budget : budgets) {
            if (budget.getCategory().equals(budgetCategory)) {
                budget.addExpense(amount, expenseCategory, date);
                if (budget.isBudgetExceeded()) {
                    System.out.println("Warning: Budget " + budgetCategory + " exceeded!");
                }
                return;
            }
        }
        System.out.println("Budget not found: " + budgetCategory);
    }

    public float getTotalIncome() {
        return Income.getTotalIncome() - getTotalSpending();
    }

    public float getAvailableIncome() {
        return Income.getTotalIncome() - allocatedIncome;
    }

    public List<Income> getIncomes() {
        return Income.getAllIncomes();
    }

    public List<Expense> getAllExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        for (Budget budget : budgets) {
            allExpenses.addAll(budget.getExpenses());
        }
        return allExpenses;
    }

    public float getTotalSpending() {
        float total = 0;
        for (Budget budget : budgets) {
            total += budget.getTotalExpenses();
        }
        return total;
    }

    public List<Budget> getBudgets() {
        return new ArrayList<>(budgets);
    }
} 