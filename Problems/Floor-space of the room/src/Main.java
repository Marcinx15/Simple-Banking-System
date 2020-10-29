import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);
        String roomShape = in.nextLine();
        double area = 0.0;
        switch (roomShape) {
            case "triangle":
                area = heronFormula(in.nextDouble(), in.nextDouble(), in.nextDouble());
                break;
            case "rectangle":
                area = in.nextDouble() * in.nextDouble();
                break;
            case "circle":
                area = 3.14 * Math.pow(in.nextDouble(),2.0);
                break;
        }

        System.out.println(area);
    }

    public static double heronFormula(double a, double b, double c) {
        double p = (a + b + c) / 2.0;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }
}