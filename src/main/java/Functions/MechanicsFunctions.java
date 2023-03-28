package Functions;

import java.util.Random;

public class MechanicsFunctions {

    public static double generateDoubleBetween(double min, double max) {
        if(min == max) {
            return min;
        }
        else if(max < min) {
            return new Random().nextDouble(min - max) + max;
        }
        else {
            return new Random().nextDouble(max - min) + min +1;
        }
    }
}
