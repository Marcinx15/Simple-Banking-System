import java.util.Arrays;
import java.util.OptionalInt;

/* Please, do not rename it */
class Problem {

    public static void main(String[] args) {
        String operator = args[0];
        switch (operator) {
            case "MAX":
                max(args);
                break;
            case "MIN":
                min(args);
                break;
            case "SUM":
                sum(args);
                break;
            default:
                System.out.println("Not recognized command");
                break;
        }
    }

    public static void max(String[] args) {
        OptionalInt max = Arrays.stream(args)
                        .skip(1)
                        .mapToInt(Integer::parseInt)
                        .max();

        max.ifPresent(System.out::println);
    }

    public static void min(String[] args) {
        OptionalInt min = Arrays.stream(args)
                .skip(1)
                .mapToInt(Integer::parseInt)
                .min();

        min.ifPresent(System.out::println);
    }

    public static void sum(String[] args) {
        int sum = Arrays.stream(args)
                .skip(1)
                .mapToInt(Integer::parseInt)
                .sum();

        System.out.println(sum);
    }

}
