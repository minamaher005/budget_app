package com.budget.budget_management;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reminder {
    private static int nextId = 1;
    private int id;
    private String description;
    private Date dateTime;
    private boolean isActive;
    private double encryptedDate;

    public Reminder(String description, Date dateTime) {
        this.id = nextId++;
        this.description = description;
        this.dateTime = dateTime;
        this.isActive = true;
        encrypte_date(); // Automatically encrypt on creation
    }

    public void encrypte_date() {
        if (dateTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            this.encryptedDate = Double.parseDouble(sdf.format(dateTime));
        }
    }

    public boolean validate() {
        
        if (description == null || description.trim().isEmpty() || dateTime == null)
            return false;

        // Encrypt current date for comparison
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        double currentEncryptedDate = Double.parseDouble(sdf.format(new Date()));

        return encryptedDate <= currentEncryptedDate;
    }

    public void save() {
        
            System.out.println("Reminder saved: " + this);
        
    }

    public void scheduleNotification() {
    if (isActive) {
        System.out.println("\n!!!!! ALERT !!!!!");
        System.out.println("Reminder Description: " + description);
        System.out.println("Scheduled For       : " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateTime));
        System.out.println();
        isActive = false; // Deactivate after notification
    }
}


    @Override
    public String toString() {
        return "Reminder{id=" + id + ", description='" + description + "', dateTime=" + dateTime + ", isActive=" + isActive + "}";
    }

    // Getters and setters
    public int getId() { return id; }
    public String getDescription() { return description; }
    public Date getDateTime() { return dateTime; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public static int getNextId() { return nextId; }
    public static void setNextId(int nextId) { Reminder.nextId = nextId; }
    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }
    public double getEncryptedDate() { return encryptedDate; }
    public void setEncryptedDate(double encryptedDate) { this.encryptedDate = encryptedDate; }
}
