package utils;

public class Formatter {

    public static double getRoundedPrice(double p) {

        if (p % 0.5 == 0) {
            return p;
        } else {
            return Math.floor(p);
        }


    }

    public static int getNumber(String text) {

        text = text.replaceAll("\\D", "");

        return Integer.parseInt(text);

    }

}
