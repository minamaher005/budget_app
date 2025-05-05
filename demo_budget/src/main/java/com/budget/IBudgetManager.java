package budget_management;

import java.util.Date;
import java.util.List;

public interface IBudgetManager {
    void setBudgetLimit(float amount);
    void addExpense(float amount, String category, Date date);
    void editExpense(int expenseId, float amount, String category, Date date);
    void deleteExpense(int expenseId);
    void setExpenseRecurring(int expenseId, boolean isRecurring);
    List<String> getRecommendations();
}