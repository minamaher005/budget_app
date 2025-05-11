package com.budget.income_management;

import java.util.Date;

public class IncomeFactory {
    public enum IncomeType {
        SALARY,
        INVESTMENT,
        OTHER
    }
    public static IIncomeSource createIncome(IncomeType type, float amount, Date date, String name) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be Empty");
        }

        switch (type) {
            case SALARY:
                return new SalaryIncome(amount, date, name);
            case INVESTMENT:
                return new InvestmentIncome(amount, date, name);
            case OTHER:
                return new OtherIncome(amount, date, name);
            default:
                throw new IllegalArgumentException("Invalid income type");
        }
    }
} 
    

