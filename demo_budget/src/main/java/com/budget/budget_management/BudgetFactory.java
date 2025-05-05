package com.budget.budget_management;

public class BudgetFactory {
    public IBudgetManager createBudget(String type) {
        switch (type.toLowerCase()) {
            case "personal":
                return new PersonalBudget();
            case "business":
                return new BusinessBudget();
            default:
                throw new IllegalArgumentException("Invalid budget type: " + type);
        }
    }
}