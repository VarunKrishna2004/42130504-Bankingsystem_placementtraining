package bankserver;

import java.sql.*;
import java.util.Scanner;

public class banking {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Connect to the MySQL database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bankingsystem","root","VarunK_6");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Banking System");
            System.out.print("Enter Email: ");
            String email = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();

            int acc_no = authenticateUser(email, password);
            if (acc_no != 0) {
                System.out.println("Login successful!");
                System.out.print("Enter (1) to Check Balance, (2) to Deposit Money, or (3) to Withdraw Money: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        checkBalance(acc_no);
                        break;
                    case 2:
                        depositMoney(acc_no);
                        break;
                    case 3:
                        withdrawMoney(acc_no);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            } else {
                System.out.println("Invalid login credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int authenticateUser(String email, String password) {
        try {
            String query = "SELECT acc_no FROM accounts WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("acc_no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void checkBalance(int acc_no) {
        try {
            String query = "SELECT balance FROM accounts WHERE acc_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, acc_no);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Your current balance is: " + resultSet.getBigDecimal("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void depositMoney(int acc_no) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();

            String queryUpdateBalance = "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?";
            PreparedStatement updateBalanceStmt = connection.prepareStatement(queryUpdateBalance);
            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setInt(2, acc_no);
            updateBalanceStmt.executeUpdate();

            System.out.println("Deposit successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void withdrawMoney(int acc_no) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();

            String queryCheckBalance = "SELECT balance FROM accounts WHERE acc_no = ?";
            PreparedStatement checkBalanceStmt = connection.prepareStatement(queryCheckBalance);
            checkBalanceStmt.setInt(1, acc_no);
            ResultSet resultSet = checkBalanceStmt.executeQuery();

            if (resultSet.next() && resultSet.getBigDecimal("balance").doubleValue() >= amount) {
                String queryUpdateBalance = "UPDATE accounts SET balance = balance - ? WHERE acc_no = ?";
                PreparedStatement updateBalanceStmt = connection.prepareStatement(queryUpdateBalance);
                updateBalanceStmt.setDouble(1, amount);
                updateBalanceStmt.setInt(2, acc_no);
                updateBalanceStmt.executeUpdate();

                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient funds.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
