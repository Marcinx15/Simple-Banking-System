import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Double random =  Math.random();
        DecimalFormat df = new DecimalFormat("0.000000000");
        df.setRoundingMode(RoundingMode.CEILING);
        Double number = Double.parseDouble(df.format(random)) * Math.pow(10,9);

    }
}