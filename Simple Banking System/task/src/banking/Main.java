package banking;

import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        String databaseName = args[1];
        createCardTable(databaseName);

        Bank bank = new Bank();
        boolean exit = false;
        Card customerCard;

        while (true) {
            printEntryMenu();
            int choice = in.nextInt();
            System.out.println();
            switch (choice) {
                case 0:
                    System.out.println("Bye!");
                    return;
                case 1:
                    Card.createNewCard(bank, databaseName);
                    break;
                case 2:
                    customerCard = bank.logIn(databaseName);
                    if (customerCard != null) exit = userMenu(customerCard, bank, databaseName);
                    if (exit) return;
            }
        }
    }

    public static void printEntryMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    public static void printUserMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    public static boolean userMenu(Card userCard, Bank bank, String databaseName) {
        while (true) {
            printUserMenu();
            int choice = in.nextInt();
            System.out.println();
            switch (choice) {
                case 1:
                    System.out.println("Balance: " + userCard.getBalance());
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Enter income:");
                    int amount = in.nextInt();
                    userCard.addIncome(amount,databaseName);
                    System.out.println("Income was added!");
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Transfer");
                    userCard.doTransfer(databaseName);
                    System.out.println();
                    break;
                case 4:
                    bank.deleteCardFromDatabase(userCard,databaseName);
                    System.out.println("The account has been closed!");
                    System.out.println();
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    System.out.println();
                    return false;
                case 0:
                    System.out.println("Bye!");
                    return true;
            }
        }
    }

    public static Connection connectToDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:" + name;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        return dataSource.getConnection();

    }

    public static void createCardTable(String databaseName){
        try (
                Connection connection = connectToDatabase(databaseName);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0)");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}

