

import java.util.Date;

public abstract class Income implements IIncomeSource {
    protected float amount;
    protected Date date;
    protected String name;

    public Income(float amount, Date date, String name) {
        this.amount = amount;
        this.date = date;
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
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
