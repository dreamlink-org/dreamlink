package dreamlink.utility.maths;

public class IntMaths {

    public static int clamp(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }
    
}
