package com.budget.income_management;

public interface IIncomeSource {
    void recordIncome();
    float calculateTaxes();
}