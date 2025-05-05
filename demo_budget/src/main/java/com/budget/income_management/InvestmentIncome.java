package com.budget.income_management;

import java.util.Date;

public class InvestmentIncome extends Income {
    public InvestmentIncome(float amount, Date date, String name) {
        super(amount, date, name);
    }

    @Override
    public void recordIncome() {
        System.out.println("Recording investment income: " + name + ", amount: " + amount);
    }

    @Override
    public float calculateTaxes() {
        return TaxCalculator.calculateTax(amount, "investment");
    }
}