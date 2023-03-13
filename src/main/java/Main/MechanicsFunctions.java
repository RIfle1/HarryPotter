package Main;

import java.util.Random;

public class MechanicsFunctions {

    public static double generateDoubleBetween(double min, double max) {
        return new Random().nextDouble(max - min) + min +1;
    }
}
