package com.budget;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.budget.budget_management.Budget;
import com.budget.budget_management.BudgetFactory.BudgetType;
import com.budget.budget_management.Expense;
import com.budget.budget_management.Reminder;
import com.budget.income_management.IncomeFactory.IncomeType;

public class BudgetAppGUI extends JFrame {
    private User currentUser;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel loginPanel;
    private JPanel mainMenuPanel;
    private JTable budgetTable;
    private JTable expenseTable;
    private DefaultTableModel budgetTableModel;
    private DefaultTableModel expenseTableModel;
    private Timer reminderTimer;
    private TrayIcon trayIcon;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color SIDEBAR_COLOR = new Color(52, 73, 94);
    private static final Color SIDEBAR_TEXT_COLOR = new Color(236, 240, 241);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Dark mode colors
    private static final Color DARK_PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color DARK_SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color DARK_BACKGROUND_COLOR = new Color(33, 33, 33);
    private static final Color DARK_CARD_COLOR = new Color(48, 48, 48);
    private static final Color DARK_TEXT_COLOR = new Color(236, 240, 241);
    private static final Color DARK_SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DARK_WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DARK_DANGER_COLOR = new Color(231, 76, 60);
    private static final Color DARK_SIDEBAR_COLOR = new Color(38, 38, 38);
    private static final Color DARK_SIDEBAR_TEXT_COLOR = new Color(236, 240, 241);

    private boolean isDarkMode = false;

    private Color getPrimaryColor() {
        return isDarkMode ? DARK_PRIMARY_COLOR : PRIMARY_COLOR;
    }

    private Color getSecondaryColor() {
        return isDarkMode ? DARK_SECONDARY_COLOR : SECONDARY_COLOR;
    }

    private Color getBackgroundColor() {
        return isDarkMode ? DARK_BACKGROUND_COLOR : BACKGROUND_COLOR;
    }

    private Color getCardColor() {
        return isDarkMode ? DARK_CARD_COLOR : CARD_COLOR;
    }

    private Color getTextColor() {
        return isDarkMode ? DARK_TEXT_COLOR : TEXT_COLOR;
    }

    private Color getSuccessColor() {
        return isDarkMode ? DARK_SUCCESS_COLOR : SUCCESS_COLOR;
    }

    private Color getWarningColor() {
        return isDarkMode ? DARK_WARNING_COLOR : WARNING_COLOR;
    }

    private Color getDangerColor() {
        return isDarkMode ? DARK_DANGER_COLOR : DANGER_COLOR;
    }

    private Color getSidebarColor() {
        return isDarkMode ? DARK_SIDEBAR_COLOR : SIDEBAR_COLOR;
    }

    private Color getSidebarTextColor() {
        return isDarkMode ? DARK_SIDEBAR_TEXT_COLOR : SIDEBAR_TEXT_COLOR;
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        mainMenuPanel.removeAll();
        mainMenuPanel.add(createMainMenuPanel());
        mainMenuPanel.revalidate();
        mainMenuPanel.repaint();
    }

    public BudgetAppGUI() {
        setTitle("Budget Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize components
        initializeComponents();
        setupLayout();
        setupSystemTray();
        startReminderCheck();
        showLoginPanel();
    }

    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize tables
        String[] budgetColumns = {"Category", "Limit", "Total Expenses", "Status"};
        budgetTableModel = new DefaultTableModel(budgetColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        budgetTable = new JTable(budgetTableModel);

        String[] expenseColumns = {"ID", "Amount", "Category", "Date", "Recurring"};
        expenseTableModel = new DefaultTableModel(expenseColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        expenseTable = new JTable(expenseTableModel);
        
        loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "LOGIN");
        
        add(mainPanel);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Budget Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        panel.add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            currentUser = User.login(username, password);
            if (currentUser != null) {
                // Load saved user data
                loadUserState(currentUser);
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Please try again.");
            }
        });

        signupButton.addActionListener(e -> showSignupDialog());

        return panel;
    }

    private void showSignupDialog() {
        JDialog dialog = new JDialog(this, "Sign Up", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton submitButton = new JButton("Submit");

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        dialog.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            currentUser = new User(name, email, password);
            if (currentUser.signup()) {
                // Save initial user state
                saveCurrentUserState();
                JOptionPane.showMessageDialog(dialog, "Signup Successful!");
                dialog.dispose();
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(dialog, "Signup failed. User might already exist.");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(dialog);
        dialog.setVisible(true);
    }

    private void showMainMenu() {
        if (mainMenuPanel != null) {
            mainPanel.remove(mainMenuPanel);
        }
        mainMenuPanel = createMainMenuPanel();
        mainPanel.add(mainMenuPanel, "MAIN_MENU");
        cardLayout.show(mainPanel, "MAIN_MENU");
        refreshBudgetTable();
        refreshExpenseTable();
    }

    private JPanel createMainMenuPanel() {
        if (currentUser == null) {
            return new JPanel(); // Return empty panel if no user is logged in
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(getBackgroundColor());

        // Create sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Create main content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(getBackgroundColor());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create header
        JPanel header = createHeader();
        contentPanel.add(header, BorderLayout.NORTH);

        // Create dashboard with current data
        JPanel dashboard = createDashboard();
        contentPanel.add(dashboard, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createSidebar() {
        if (currentUser == null) {
            return new JPanel();
        }

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(getSidebarColor());
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // User profile section
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(getSidebarColor());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel userLabel = new JLabel(currentUser.getName());
        userLabel.setFont(HEADER_FONT);
        userLabel.setForeground(getSidebarTextColor());
        profilePanel.add(userLabel);

        JLabel emailLabel = new JLabel(currentUser.getEmail());
        emailLabel.setFont(SMALL_FONT);
        emailLabel.setForeground(getSidebarTextColor());
        profilePanel.add(emailLabel);

        sidebar.add(profilePanel);
        sidebar.add(Box.createVerticalStrut(20));

        // Navigation buttons
        String[] navItems = {
            "Dashboard", "Budgets", "Expenses", "Income", "Reminders", "Dark Mode"
        };

        for (String item : navItems) {
            JButton navButton = createNavButton(item);
            sidebar.add(navButton);
            sidebar.add(Box.createVerticalStrut(10));
        }

        // Logout button at bottom
        sidebar.add(Box.createVerticalGlue());
        JButton logoutButton = createNavButton("Logout");
        logoutButton.addActionListener(e -> logout());
        sidebar.add(logoutButton);
        sidebar.add(Box.createVerticalStrut(20));

        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getSidebarColor().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(getSidebarColor().brighter());
                } else {
                    g2.setColor(getSidebarColor());
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(NORMAL_FONT);
        button.setForeground(getSidebarTextColor());
        button.setBackground(getSidebarColor());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(230, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners based on button text
        switch (text) {
            case "Dashboard":
                button.addActionListener(e -> {
                    mainMenuPanel.removeAll();
                    mainMenuPanel.add(createMainMenuPanel());
                    mainMenuPanel.revalidate();
                    mainMenuPanel.repaint();
                });
                break;
            case "Budgets":
                button.addActionListener(e -> {
                    showCreateBudgetDialog();
                    mainMenuPanel.removeAll();
                    mainMenuPanel.add(createMainMenuPanel());
                    mainMenuPanel.revalidate();
                    mainMenuPanel.repaint();
                });
                break;
            case "Expenses":
                button.addActionListener(e -> {
                    showAddExpenseDialog();
                    mainMenuPanel.removeAll();
                    mainMenuPanel.add(createMainMenuPanel());
                    mainMenuPanel.revalidate();
                    mainMenuPanel.repaint();
                });
                break;
            case "Income":
                button.addActionListener(e -> {
                    showAddIncomeDialog();
                    mainMenuPanel.removeAll();
                    mainMenuPanel.add(createMainMenuPanel());
                    mainMenuPanel.revalidate();
                    mainMenuPanel.repaint();
                });
                break;
            case "Reminders":
                button.addActionListener(e -> showManageRemindersDialog());
                break;
            case "Dark Mode":
                button.addActionListener(e -> {
                    toggleDarkMode();
                    button.setText(isDarkMode ? "Light Mode" : "Dark Mode");
                });
                break;
        }

        return button;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(getCardColor());
        header.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(getTextColor());
        header.add(titleLabel, BorderLayout.WEST);

        // Add date/time
        JLabel dateLabel = new JLabel(new SimpleDateFormat("EEEE, MMMM d, yyyy").format(new Date()));
        dateLabel.setFont(NORMAL_FONT);
        dateLabel.setForeground(getTextColor());
        header.add(dateLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createDashboard() {
        JPanel dashboard = new JPanel(new GridBagLayout());
        dashboard.setBackground(getBackgroundColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Summary cards with current data
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        JPanel summaryCard = createSummaryCard();
        dashboard.add(summaryCard, gbc);

        // Budget and Expense tables
        gbc.gridy = 1;
        gbc.weighty = 0.7;
        JPanel tablesPanel = createTablesPanel();
        dashboard.add(tablesPanel, gbc);

        return dashboard;
    }

    private JPanel createSummaryCard() {
        JPanel card = new JPanel(new GridLayout(1, 4, 15, 0));
        card.setBackground(getCardColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Get current data
        float totalIncome = currentUser.getBudgetManager().getTotalIncome();
        float totalExpenses = currentUser.getBudgetManager().getTotalSpending();
        float availableBalance = currentUser.getBudgetManager().getAvailableIncome();
        int activeBudgets = currentUser.getBudgetManager().getBudgets().size();

        // Total Income
        card.add(createMetricPanel("Total Income", 
            String.format("$%.2f", totalIncome),
            getSuccessColor()));

        // Total Expenses
        card.add(createMetricPanel("Total Expenses",
            String.format("$%.2f", totalExpenses),
            getDangerColor()));

        // Available Balance
        card.add(createMetricPanel("Available Balance",
            String.format("$%.2f", availableBalance),
            getPrimaryColor()));

        // Active Budgets
        card.add(createMetricPanel("Active Budgets",
            String.valueOf(activeBudgets),
            getSecondaryColor()));

        return card;
    }

    private JPanel createMetricPanel(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(getCardColor());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SMALL_FONT);
        titleLabel.setForeground(getTextColor());

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(HEADER_FONT);
        valueLabel.setForeground(color);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valueLabel);

        return panel;
    }

    private JPanel createTablesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(getBackgroundColor());

        // Budget Table
        JPanel budgetPanel = createTableCard("Budgets", budgetTable);
        panel.add(budgetPanel);

        // Expense Table
        JPanel expensePanel = createTableCard("Recent Expenses", expenseTable);
        panel.add(expensePanel);

        // Refresh tables with current data
        refreshBudgetTable();
        refreshExpenseTable();

        return panel;
    }

    private JPanel createTableCard(String title, JTable table) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(getCardColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(getTextColor());
        card.add(titleLabel, BorderLayout.NORTH);

        // Style the table
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setRowHeight(30);
        table.getTableHeader().setFont(NORMAL_FONT);
        table.getTableHeader().setBackground(getCardColor());
        table.getTableHeader().setForeground(getTextColor());
        table.setFont(NORMAL_FONT);
        table.setBackground(getCardColor());
        table.setForeground(getTextColor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void showAddIncomeDialog() {
        JDialog dialog = new JDialog(this, "Add Income", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<IncomeType> typeCombo = new JComboBox<>(IncomeType.values());
        JTextField amountField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        dialog.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        dialog.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            try {
                IncomeType type = (IncomeType) typeCombo.getSelectedItem();
                float amount = Float.parseFloat(amountField.getText());
                String name = nameField.getText();
                
                currentUser.getBudgetManager().createIncome(type, amount, new Date(), name);
                // Save user data after adding income
                saveCurrentUserState();
                JOptionPane.showMessageDialog(dialog, "Income added successfully!");
                dialog.dispose();
                mainMenuPanel.removeAll();
                mainMenuPanel.add(createMainMenuPanel());
                mainMenuPanel.revalidate();
                mainMenuPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid amount.");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showCreateBudgetDialog() {
        JDialog dialog = new JDialog(this, "Create Budget", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<BudgetType> typeCombo = new JComboBox<>(BudgetType.values());
        JTextField categoryField = new JTextField(20);
        JTextField limitField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        float availableFunds = currentUser.getBudgetManager().getAvailableIncome();
        JLabel availableFundsLabel = new JLabel(String.format("Available Funds: $%.2f", availableFunds));
        availableFundsLabel.setFont(new Font("Arial", Font.BOLD, 12));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dialog.add(availableFundsLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        dialog.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        dialog.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        dialog.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Limit:"), gbc);
        gbc.gridx = 1;
        dialog.add(limitField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        dialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            try {
                BudgetType type = (BudgetType) typeCombo.getSelectedItem();
                String category = categoryField.getText();
                float limit = Float.parseFloat(limitField.getText());
                
                if (limit > availableFunds) {
                    JOptionPane.showMessageDialog(dialog, 
                        String.format("Insufficient funds to create budget. Available: $%.2f", availableFunds),
                        "Budget Creation Failed",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                currentUser.getBudgetManager().createBudget(type, category, limit);
                // Save user data after creating budget
                saveCurrentUserState();
                JOptionPane.showMessageDialog(dialog, "Budget created successfully!");
                dialog.dispose();
                refreshBudgetTable();
                mainMenuPanel.removeAll();
                mainMenuPanel.add(createMainMenuPanel());
                mainMenuPanel.revalidate();
                mainMenuPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid limit amount.");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddExpenseDialog() {
        JDialog dialog = new JDialog(this, "Add Expense", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> budgetCategoryCombo = new JComboBox<>(
            currentUser.getBudgetManager().getBudgets().stream()
                .map(Budget::getCategory)
                .toArray(String[]::new)
        );
        JTextField expenseCategoryField = new JTextField(20);
        JTextField amountField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Budget Category:"), gbc);
        gbc.gridx = 1;
        dialog.add(budgetCategoryCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Expense Category:"), gbc);
        gbc.gridx = 1;
        dialog.add(expenseCategoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        dialog.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            try {
                String budgetCategory = (String) budgetCategoryCombo.getSelectedItem();
                String expenseCategory = expenseCategoryField.getText();
                float amount = Float.parseFloat(amountField.getText());
                
                currentUser.getBudgetManager().addExpenseToBudget(budgetCategory, amount, expenseCategory, new Date());
                // Save user data after adding expense
                saveCurrentUserState();
                JOptionPane.showMessageDialog(dialog, "Expense added successfully!");
                dialog.dispose();
                refreshExpenseTable();
                refreshBudgetTable();
                mainMenuPanel.removeAll();
                mainMenuPanel.add(createMainMenuPanel());
                mainMenuPanel.revalidate();
                mainMenuPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid amount.");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showManageRemindersDialog() {
        JDialog dialog = new JDialog(this, "Manage Reminders", true);
        dialog.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addReminderBtn = new JButton("Add Reminder");
        buttonPanel.add(addReminderBtn);

        DefaultTableModel reminderTableModel = new DefaultTableModel(
            new String[]{"Description", "Date & Time", "Status"}, 0
        );
        JTable reminderTable = new JTable(reminderTableModel);

        // Populate reminder table
        for (Budget budget : currentUser.getBudgetManager().getBudgets()) {
            for (Reminder reminder : budget.getReminders()) {
                reminderTableModel.addRow(new Object[]{
                    reminder.getDescription(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(reminder.getDateTime()),
                    reminder.isActive() ? "Active" : "Inactive"
                });
            }
        }

        addReminderBtn.addActionListener(e -> {
            JDialog addDialog = new JDialog(dialog, "Add Reminder", true);
            addDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            JTextField descriptionField = new JTextField(20);
            
            // Create date and time spinner
            JSpinner dateTimeSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor editor = new JSpinner.DateEditor(dateTimeSpinner, "yyyy-MM-dd HH:mm");
            dateTimeSpinner.setEditor(editor);
            
            JButton submitButton = new JButton("Submit");

            gbc.gridx = 0;
            gbc.gridy = 0;
            addDialog.add(new JLabel("Description:"), gbc);
            gbc.gridx = 1;
            addDialog.add(descriptionField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            addDialog.add(new JLabel("Date & Time:"), gbc);
            gbc.gridx = 1;
            addDialog.add(dateTimeSpinner, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            addDialog.add(submitButton, gbc);

            submitButton.addActionListener(ev -> {
                try {
                    String description = descriptionField.getText();
                    if (description.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(addDialog, "Please enter a description for the reminder.");
                        return;
                    }
                    
                    Date dateTime = (Date) dateTimeSpinner.getValue();
                    if (dateTime.before(new Date())) {
                        JOptionPane.showMessageDialog(addDialog, "Please select a future date and time.");
                        return;
                    }
                    
                    Reminder reminder = new Reminder(description, dateTime);
                    for (Budget budget : currentUser.getBudgetManager().getBudgets()) {
                        budget.addReminder(reminder);
                    }
                    
                    reminderTableModel.addRow(new Object[]{
                        description,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateTime),
                        "Active"
                    });
                    
                    addDialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addDialog, "Error creating reminder: " + ex.getMessage());
                }
            });

            addDialog.pack();
            addDialog.setLocationRelativeTo(dialog);
            addDialog.setVisible(true);
        });

        dialog.add(buttonPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(reminderTable), BorderLayout.CENTER);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showTotalIncome() {
        float totalIncome = currentUser.getBudgetManager().getTotalIncome();
        JOptionPane.showMessageDialog(this, 
            String.format("Total Income: $%.2f", totalIncome),
            "Total Income",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTotalSpending() {
        float totalSpending = currentUser.getBudgetManager().getTotalSpending();
        JOptionPane.showMessageDialog(this, 
            String.format("Total Spending: $%.2f", totalSpending),
            "Total Spending",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshBudgetTable() {
        if (budgetTableModel != null) {
            budgetTableModel.setRowCount(0);
            for (Budget budget : currentUser.getBudgetManager().getBudgets()) {
                budgetTableModel.addRow(new Object[]{
                    budget.getCategory(),
                    String.format("$%.2f", budget.getBudgetLimit()),
                    String.format("$%.2f", budget.getTotalExpenses()),
                    budget.isBudgetExceeded() ? "Exceeded" : "Within Limit"
                });
            }
            budgetTableModel.fireTableDataChanged();
        }
    }

    private void refreshExpenseTable() {
        expenseTableModel.setRowCount(0);
        for (Expense expense : currentUser.getBudgetManager().getAllExpenses()) {
            expenseTableModel.addRow(new Object[]{
                expense.getId(),
                String.format("$%.2f", expense.getAmount()),
                expense.getCategory(),
                new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate()),
                expense.isRecurring() ? "Yes" : "No"
            });
        }
    }

    private void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void logout() {
        if (currentUser != null) {
            // Save current state before logging out
            saveCurrentUserState();
        }
        currentUser = null;
        showLoginPanel();
    }

    private void saveCurrentUserState() {
        try {
            // Create a directory for user data if it doesn't exist
            File userDataDir = new File("user_data");
            if (!userDataDir.exists()) {
                userDataDir.mkdir();
            }

            // Save user data to a file
            File userFile = new File(userDataDir, currentUser.getEmail() + ".dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile))) {
                oos.writeObject(currentUser);
            }
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    private void loadUserState(User user) {
        try {
            File userFile = new File("user_data", user.getEmail() + ".dat");
            if (userFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
                    User savedUser = (User) ois.readObject();
                    // Update current user with saved data
                    currentUser = savedUser;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }

    private void setupSystemTray() {
        if (SystemTray.isSupported()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                PopupMenu popup = new PopupMenu();
                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(e -> System.exit(0));
                popup.add(exitItem);

                trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon.png")), 
                    "Budget Management System", popup);
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
            } catch (Exception e) {
                System.err.println("Could not setup system tray: " + e.getMessage());
            }
        }
    }

    private void startReminderCheck() {
        reminderTimer = new Timer(true);
        reminderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentUser != null) {
                    checkReminders();
                }
            }
        }, 0, 60000); // Check every minute
    }

    private void checkReminders() {
        SwingUtilities.invokeLater(() -> {
            for (Budget budget : currentUser.getBudgetManager().getBudgets()) {
                for (Reminder reminder : budget.getReminders()) {
                    if (reminder.isActive() && reminder.validate()) {
                        // Set reminder to inactive before showing the alert
                        reminder.setActive(false);
                        showReminderAlert(reminder);
                    }
                }
            }
        });
    }

    private void showReminderAlert(Reminder reminder) {
        if (trayIcon != null) {
            trayIcon.displayMessage(
                "Budget Reminder",
                reminder.getDescription(),
                TrayIcon.MessageType.INFO
            );
        }

        JDialog alertDialog = new JDialog(this, "Reminder Alert", true);
        alertDialog.setLayout(new BorderLayout());
        
        JPanel messagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Reminder Alert");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel descriptionLabel = new JLabel(reminder.getDescription());
        JLabel timeLabel = new JLabel("Scheduled for: " + 
            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(reminder.getDateTime()));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        messagePanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        messagePanel.add(descriptionLabel, gbc);
        
        gbc.gridy = 2;
        messagePanel.add(timeLabel, gbc);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            alertDialog.dispose();
            // Refresh the reminders dialog
            showManageRemindersDialog();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        
        alertDialog.add(messagePanel, BorderLayout.CENTER);
        alertDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        alertDialog.pack();
        alertDialog.setLocationRelativeTo(this);
        alertDialog.setVisible(true);
    }

    @Override
    public void dispose() {
        if (reminderTimer != null) {
            reminderTimer.cancel();
        }
        if (trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BudgetAppGUI().setVisible(true);
        });
    }
}