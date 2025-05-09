package com.budget.income_management;

import java.util.Date;

public class OtherIncome extends Income {
    public OtherIncome(float amount, Date date, String name) {
        super(amount, date, name);
    }

    @Override
    public void recordIncome() {
        System.out.println("Recording other income: " + name + ", amount: " + amount);
    }
}