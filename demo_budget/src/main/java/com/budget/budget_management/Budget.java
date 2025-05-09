package com.budget.budget_management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Budget implements IBudgetManager {
    protected float limit;
    protected String category;
    protected List<Expense> expenses;
    protected float totalExpenses;
    protected List<Reminder> reminders;

    public Budget(String category) {
        this.category = category;
        this.expenses = new ArrayList<>();
        this.totalExpenses = 0;
        this.reminders = new ArrayList<>();
    }

    @Override
    public void setBudgetLimit(float amount) {
        this.limit = amount;
    }

    public float getBudgetLimit() {
        return limit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isBudgetExceeded() {
        return totalExpenses > limit;
    }

    @Override
    public void addExpense(float amount, String category, Date date) {
        Expense expense = new Expense(amount, category, date);
        if (expense.validate()) {
            expenses.add(expense);
            expense.save();
            totalExpenses += amount;
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
        List<String> recommendations = new ArrayList<>();
        if (isBudgetExceeded()) {
            recommendations.add("Budget exceeded! Consider reducing expenses in " + category);
        }
        return recommendations;
    }

    public float getTotalExpenses() {
        return totalExpenses;
    }

    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }

    public void addReminder(Reminder reminder) {
        if (reminder != null) {
            reminders.add(reminder);
            reminder.save();
        }
    }

    public List<Reminder> getReminders() {
        return new ArrayList<>(reminders);
    }

    public void validateReminders() {
        for (Reminder reminder : reminders) {
            if (reminder.validate()) {
                reminder.scheduleNotification();
            }
        }
    }
}