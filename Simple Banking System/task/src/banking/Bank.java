package banking;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Bank {
    public static int nextId = 1;

    public boolean addCardToDatabase(Card card, String databaseName) {
        try(
                Connection connection = Main.connectToDatabase(databaseName);
                Statement statement = connection.createStatement()
        ) {
            var resultSet =  statement.executeQuery(
                    "SELECT * FROM card" + " WHERE number = " + card.getNumber());

            if(resultSet.next())
                return false;

            statement.executeUpdate("INSERT INTO card VALUES " +
                    "(" + nextId + ", '" + card.getNumber() +"', '" + card.getPin() +"', 0)");

            nextId++;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        printAddingMessage(card);
        return true;
    }

    public boolean deleteCardFromDatabase(Card card, String databaseName) {
        try(
                Connection connection = Main.connectToDatabase(databaseName);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("DELETE FROM card WHERE number = " + card.getNumber());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return true;
    }

    private void printAddingMessage(Card card) {
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(card.getNumber());
        System.out.println("Your card PIN:");
        System.out.println(card.getPin());
        System.out.println();
    }

    public Card logIn(String databaseName) {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter your card number:");
        String cardNumber = in.nextLine();
        System.out.println("Your card PIN:");
        String cardPin = in.nextLine();
        System.out.println();

        if(checkCredentials(cardNumber, cardPin, databaseName)) {
            System.out.println("You have successfully logged in!");
            System.out.println();
            return Card.createCardFromDatabase(cardNumber, cardPin, databaseName);
        }

        System.out.println("Wrong card number or PIN!");
        System.out.println();
        return null;
    }

    private boolean checkCredentials(String cardNumber, String PIN, String databaseName) {
        try (Connection connection = Main.connectToDatabase(databaseName);
            Statement statement = connection.createStatement()
        ) {
            var resultSet =  statement.executeQuery(
                    "SELECT * FROM card" + " WHERE number = " +
                       cardNumber + " AND pin = " + PIN);
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public static boolean doesCardExistInDatabase(String cardNumber, String databaseName) {
        try (Connection connection = Main.connectToDatabase(databaseName);
             Statement statement = connection.createStatement()
        ) {
            var resultSet =  statement.executeQuery(
                    "SELECT * FROM card" + " WHERE number = " + cardNumber);
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

}
