package com.budget.budget_management;
public class BudgetFactory {
    public enum BudgetType {
        PERSONAL,
        BUSINESS
    }
    public static IBudgetManager createBudget(BudgetType type) {
        switch (type) {
            case PERSONAL:
                return new PersonalBudget();
            case BUSINESS:
                return new BusinessBudget();
            default:
                throw new IllegalArgumentException("Invalid budget type");
        }
    }
}




