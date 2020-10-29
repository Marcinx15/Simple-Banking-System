package banking;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Card {
    private String number;
    private String pin;
    private int balance = 0;


    private Card(){}

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void changeBalance(int amount){
        balance += amount;
    }

    private String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private String generateAccountNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append("400000");
        builder.append(generateRandomDigits(9));
        builder.append(generateControlSumNumber(builder.toString()));
        return builder.toString();
    }

    private String generatePin(){
       return generateRandomDigits(4);
    }

    public static int generateControlSumNumber(String accountNumber) {
        char[] numbers = accountNumber.toCharArray();
        int sum = IntStream.range(0, accountNumber.length())
                 .map(i -> i % 2 == 1 ? numbers[i] - 48 : (numbers[i] - 48) * 2)
                 .map(i -> i < 10 ? i : i - 9)
                 .sum();

        return (10 - sum % 10) % 10;

    }

    public static Card createNewCard(Bank bank, String databaseName) {
        boolean added = false;
        Card card = null;

        while(!added) {
            card = new Card();
            card.setNumber(card.generateAccountNumber());
            card.setPin(card.generatePin());
            added = bank.addCardToDatabase(card, databaseName);
        }

        return card;
    }

    public static Card createCardFromDatabase(String cardNumber, String PIN, String databaseName) {
        Card card = new Card();
        card.setPin(PIN);
        card.setNumber(cardNumber);

        try (
                Connection connection = Main.connectToDatabase(databaseName);
                Statement statement = connection.createStatement()
        ) {
            var resultSet =  statement.executeQuery(
                    "SELECT * FROM card" + " WHERE number = " +
                            cardNumber + " AND pin = " + PIN);

           card.changeBalance(resultSet.getInt("balance"));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return card;
    }

    public void addIncome(int amount, String databaseName) {
       try (
               Connection connection = Main.connectToDatabase(databaseName);
               Statement statement = connection.createStatement()
       ) {
           statement.executeUpdate("UPDATE card " + " SET balance = balance + " + amount +
                                      " WHERE number = " + getNumber());

           changeBalance(amount);
       } catch (SQLException exception) {
           exception.printStackTrace();
       }

    }

    private boolean isCardNumberValid(String cardNumber) {
        if(cardNumber.length() != 16)
            return false;

        return Integer.parseInt(String.valueOf(cardNumber.charAt(15))) ==
                generateControlSumNumber(cardNumber.substring(0,15));
    }

    private boolean isCardValid(String cardNumber, String databaseName) {
        if (!isCardNumberValid(cardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return false;
        }

        if (!Bank.doesCardExistInDatabase(cardNumber,databaseName)) {
            System.out.println("Such a card does not exist.");
            return false;
        }

        if(cardNumber.equals(getNumber())){
            System.out.println("You can't transfer money to the same account!");
            return false;
        }

        return true;
    }

    private boolean areFundsSufficient(int amountToTransfer){
        return amountToTransfer <= getBalance();
    }

    private void transferMoney(String receiverCardNumber, int amount, String databaseName){
        try (
                Connection connection = Main.connectToDatabase(databaseName);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("UPDATE card " + " SET balance = balance - " + amount +
                    " WHERE number = " + getNumber());

            statement.executeUpdate("UPDATE card " + " SET balance = balance + " + amount +
                    " WHERE number = " + receiverCardNumber);

            changeBalance(-amount);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public void doTransfer(String databaseName){
        System.out.println("Enter card number:");
        Scanner in = new Scanner(System.in);
        String receiverCardNumber = in.next();

        if(!isCardValid(receiverCardNumber, databaseName)) return;

        int amountToTransfer = in.nextInt();
        in.nextLine();
        if (!areFundsSufficient(amountToTransfer)) {
            System.out.println("Not enough money!");
            return;
        }

        transferMoney(receiverCardNumber, amountToTransfer, databaseName);
        System.out.println("Success!");
    }

}
