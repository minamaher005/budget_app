package com.budget.budget_management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.budget.budget_management.BudgetFactory.BudgetType;
import com.budget.income_management.IIncomeSource;
import com.budget.income_management.Income;
import com.budget.income_management.IncomeFactory;
import com.budget.income_management.IncomeFactory.IncomeType;

public class BudgetManager {
    private final List<IBudgetManager> budgets;
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
        IBudgetManager budget = BudgetFactory.createBudget(type);
        budget.setBudgetLimit(limit);
        if (budget instanceof Budget) {
            ((Budget) budget).setCategory(category);
        }
        budgets.add(budget);
        allocatedIncome += limit;
        System.out.println("Budget created: " + category + ", limit: " + limit);
        System.out.println("Remaining available income: $" + (Income.getTotalIncome() - allocatedIncome));
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

    public float getTotalIncome() {
        return Income.getTotalIncome();
    }

    public float getAvailableIncome() {
        return Income.getTotalIncome() - allocatedIncome;
    }

    public List<Income> getIncomes() {
        return Income.getAllIncomes();
    }
} 