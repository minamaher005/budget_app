# Budget Manager Application

A Java-based budget management system that helps users track their income, expenses, and manage different types of budgets.

## Features

- User Authentication
  - Login and Signup functionality
  - Secure password management

- Income Management
  - Support for multiple income types (Salary, Investment, Other)
  - Track total income
  - Income history

- Budget Management
  - Create different types of budgets (Personal, Business)
  - Set budget limits
  - Track expenses against budgets
  - Budget recommendations

- Expense Tracking
  - Add expenses to specific budgets
  - Categorize expenses
  - Track recurring expenses
  - View expense history

- Reminder System
  - Set reminders for important dates
  - Budget limit notifications
  - Expense due date reminders

## Technical Details

### Design Patterns Used

1. Factory Pattern
   - BudgetFactory: Creates different types of budgets
   - IncomeFactory: Creates different types of income sources

2. Interface Segregation
   - IBudgetManager: Interface for budget management operations
   - IIncomeSource: Interface for income management

3. Single Responsibility Principle
   - Separate classes for Budget, Income, Expense, and Reminder management
   - Clear separation of concerns between different components

### Project Structure

```
budget_app/
├── demo_budget/
│   └── src/
│       └── main/
│           └── java/
│               └── com/
│                   └── budget/
│                       ├── Main.java
│                       ├── User.java
│                       ├── budget_management/
│                       │   ├── Budget.java
│                       │   ├── BudgetFactory.java
│                       │   ├── BudgetManager.java
│                       │   ├── BusinessBudget.java
│                       │   ├── Expense.java
│                       │   ├── PersonalBudget.java
│                       │   └── Reminder.java
│                       └── income_management/
│                           ├── Income.java
│                           ├── IncomeFactory.java
│                           ├── OtherIncome.java
│                           └── SalaryIncome.java
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven (for dependency management)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/budget-manager.git
   ```

2. Navigate to the project directory:
   ```bash
   cd budget-manager
   ```

3. Compile the project:
   ```bash
   javac -d target/classes src/main/java/com/budget/**/*.java
   ```

4. Run the application:
   ```bash
   java -cp target/classes com.budget.Main
   ```

## Usage

1. Start the application
2. Create a new account or login
3. Add your income sources
4. Create budgets for different categories
5. Start tracking your expenses
6. Set reminders for important dates

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Java Design Patterns
- Object-Oriented Programming Principles
- Clean Code practices
