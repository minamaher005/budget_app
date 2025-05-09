package com.budget.income_management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Income implements IIncomeSource {
    private static final List<Income> allIncomes = new ArrayList<>();
    private static float totalIncome = 0;
    
    protected float amount;
    protected Date date;
    protected String name;

    public Income(float amount, Date date, String name) {
        this.amount = amount;
        this.date = date;
        this.name = name;
    }

    public static void addIncome(Income income) {
        allIncomes.add(income);
        totalIncome += income.getAmount();
    }

    public static float getTotalIncome() {
        return totalIncome;
    }

    public static List<Income> getAllIncomes() {
        return new ArrayList<>(allIncomes);
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public void recordIncome() {
        System.out.println("Income recorded: " + name + " - $" + amount);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 
