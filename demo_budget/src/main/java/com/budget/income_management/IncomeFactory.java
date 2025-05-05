package com.budget.income_management;

import java.util.Date;

public class IncomeFactory {
    public IIncomeSource createIncome(String type, float amount, Date date, String name) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        switch (type.toLowerCase()) {
            case "salary":
                return new SalaryIncome(amount, date, name);
            case "investment":
                return new InvestmentIncome(amount, date, name);
            case "other":
                return new OtherIncome(amount, date, name);
            default:
                throw new IllegalArgumentException("Invalid income type: " + type);
        }
    }
} 
    

