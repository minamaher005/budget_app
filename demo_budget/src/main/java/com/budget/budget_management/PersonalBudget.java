package com.budget.budget_management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// public package budget_management;

public class PersonalBudget extends Budget implements IBudgetManager {
    public PersonalBudget() {
        super("Personal");
    }

    @Override
    public void setBudgetLimit(float amount) {
        super.setBudgetLimit(amount);
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
            recommendations.add("Budget exceeded! Consider reducing expenses in " + getCategory());
        }
        return recommendations;
    }
} 
    

