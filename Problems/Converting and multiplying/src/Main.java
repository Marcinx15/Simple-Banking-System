import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);
        String input = in.nextLine();

        while (!"0".equals(input)) {
            try {
                int number = Integer.parseInt(input);
                System.out.println(number * 10);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid user input: " + input);
            }
            input = in.nextLine();
        }

    }
}
