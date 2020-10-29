import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int amount = in.nextInt();
        int acc = 0;
        for (int i = 0; i < amount; i++) {
            int number = in.nextInt();
            acc += number % 6 == 0 ? number : 0;
        }
        System.out.println(acc);
    }
}