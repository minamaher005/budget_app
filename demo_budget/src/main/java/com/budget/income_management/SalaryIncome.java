package com.budget.income_management;

import java.util.Date;

public class SalaryIncome extends Income {
    public SalaryIncome(float amount, Date date, String name) {
        super(amount, date, name);
    }

    @Override
    public void recordIncome() {
        System.out.println("Recording salary income: " + name + ", amount: " + amount);
    }
}