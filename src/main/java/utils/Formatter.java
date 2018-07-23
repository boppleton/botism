package utils;

public class Formatter {

    public static double getRoundedPrice(double p) {

        if (p % 0.5 == 0) {
            return p;
        } else {
            return Math.floor(p);
        }


    }

}
