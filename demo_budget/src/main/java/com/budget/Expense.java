package budget_management;

import java.util.Date;

public class Expense {
    private static int nextId = 1;
    private int id;
    private float amount;
    private String category;
    private Date date;
    private boolean isRecurring;

    public Expense(float amount, String category, Date date) {
        this.id = nextId++;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = false;
    }

    public int getId() { return id; }
    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { this.isRecurring = recurring; }

    public boolean validate() {
        return amount > 0 && category != null && !category.isEmpty();
    }

    public void save() {
        System.out.println("Saving expense: " + this);
    }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", amount=" + amount + ", category='" + category + "', date=" + date + ", isRecurring=" + isRecurring + "}";
    }
}