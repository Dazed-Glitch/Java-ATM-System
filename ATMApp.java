import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMApp {
    private static ATMUser currentUser = null;
    private static boolean darkMode = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMApp::showLoginScreen);
    }

    private static void showLoginScreen() {
        JFrame frame = new JFrame("ATM Login");
        frame.setSize(350, 220);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel pinLabel = new JLabel("PIN:");
        JPasswordField pinField = new JPasswordField();
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);

        JButton loginBtn = new JButton("Login");
        JButton toggleTheme = new JButton("Toggle Theme");

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String pin = new String(pinField.getPassword());
            ATMUser user = UserManager.authenticate(username, pin);
            if (user != null) {
                currentUser = user;
                frame.dispose();
                showMainMenu();
            } else {
                messageLabel.setText("Invalid username or PIN.");
            }
        });

        toggleTheme.addActionListener(e -> {
            darkMode = !darkMode;
            frame.dispose();
            showLoginScreen();
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(pinLabel);
        panel.add(pinField);

        buttonPanel.add(loginBtn);
        buttonPanel.add(toggleTheme);

        applyTheme(panel, buttonPanel, messageLabel);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(messageLabel, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showMainMenu() {
        JFrame frame = new JFrame("Welcome, " + currentUser.getUsername());
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel balanceLabel = new JLabel("Balance: $" + String.format("%.2f", currentUser.getBalance()), SwingConstants.CENTER);
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton historyBtn = new JButton("Transaction History");
        JButton changePinBtn = new JButton("Change PIN");
        JButton logoutBtn = new JButton("Logout");

        depositBtn.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog(frame, "Enter deposit amount:");
            try {
                double val = Double.parseDouble(amt);
                if (val > 0) {
                    currentUser.deposit(val);
                    balanceLabel.setText("Balance: $" + String.format("%.2f", currentUser.getBalance()));
                }
            } catch (Exception ignored) {}
        });

        withdrawBtn.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog(frame, "Enter withdrawal amount:");
            try {
                double val = Double.parseDouble(amt);
                if (currentUser.withdraw(val)) {
                    balanceLabel.setText("Balance: $" + String.format("%.2f", currentUser.getBalance()));
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient funds.");
                }
            } catch (Exception ignored) {}
        });

        historyBtn.addActionListener(e -> {
            JTextArea area = new JTextArea(String.join("\n", currentUser.getTransactionHistory()));
            area.setEditable(false);
            area.setBackground(darkMode ? new Color(45, 45, 45) : Color.WHITE);
            area.setForeground(darkMode ? Color.WHITE : Color.BLACK);
            JOptionPane.showMessageDialog(frame, new JScrollPane(area), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
        });

        changePinBtn.addActionListener(e -> {
            String oldPin = JOptionPane.showInputDialog(frame, "Enter current PIN:");
            String newPin = JOptionPane.showInputDialog(frame, "Enter new PIN:");
            if (UserManager.changePin(currentUser, oldPin, newPin)) {
                JOptionPane.showMessageDialog(frame, "PIN updated successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect current PIN.");
            }
        });

        logoutBtn.addActionListener(e -> {
            currentUser = null;
            frame.dispose();
            showLoginScreen();
        });

        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(historyBtn);
        panel.add(changePinBtn);

        bottomPanel.add(logoutBtn);

        applyTheme(panel, bottomPanel, balanceLabel);

        frame.add(balanceLabel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void applyTheme(JComponent... components) {
        Color bg = darkMode ? new Color(30, 30, 30) : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        for (JComponent comp : components) {
            comp.setBackground(bg);
            comp.setForeground(fg);
            if (comp instanceof JPanel) {
                for (Component c : ((JPanel) comp).getComponents()) {
                    if (c instanceof JComponent) {
                        c.setBackground(bg);
                        c.setForeground(fg);
                    }
                }
            }
        }
    }
}
