package com.budget.income_management;

public interface IIncomeSource {
    float getAmount();
    void recordIncome();
}