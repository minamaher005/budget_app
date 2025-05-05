package com.budget.budget_management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Budget implements IBudgetManager {
    protected float limit;
    protected String category;
    protected List<Expense> expenses;

    public Budget(String category) {
        this.category = category;
        this.expenses = new ArrayList<>();
    }

    @Override
    public void setBudgetLimit(float amount) {
        this.limit = amount;
    }

    @Override
    public void addExpense(float amount, String category, Date date) {
        Expense expense = new Expense(amount, category, date);
        if (expense.validate()) {
            expenses.add(expense);
            expense.save();
        } else {
            System.out.println("Invalid expense");
        }
    }

    @Override
    public void editExpense(int expenseId, float amount, String category, Date date) {
        for (Expense expense : expenses) {
            if (expense.getId() == expenseId) {
                expense.setAmount(amount);
                expense.setCategory(category);
                expense.setDate(date);
                if (expense.validate()) {
                    expense.save();
                } else {
                    System.out.println("Invalid expense after edit");
                }
                return;
            }
        }
        System.out.println("Expense not found");
    }

    @Override
    public void deleteExpense(int expenseId) {
        expenses.removeIf(expense -> expense.getId() == expenseId);
    }

    @Override
    public void setExpenseRecurring(int expenseId, boolean isRecurring) {
        for (Expense expense : expenses) {
            if (expense.getId() == expenseId) {
                expense.setRecurring(isRecurring);
                expense.save();
                return;
            }
        }
        System.out.println("Expense not found");
    }

    @Override
    public List<String> getRecommendations() {
        return new ArrayList<>();
    }

    public float getTotalExpenses() {
        float total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public boolean isBudgetExceeded() {
        return getTotalExpenses() > limit;
    }

    public String getCategory() {
        return category;
    }
}