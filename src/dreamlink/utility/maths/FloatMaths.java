package dreamlink.utility.maths;

public class FloatMaths {

    public static float floatMod(float value, float modulus) {
        while(value < 0) {
            value += modulus;
        }

        return value - (float)Math.floor(value / modulus) * modulus;
    }

    public static float fuzz(float value, float epsilon) {
        return Math.abs(value) < epsilon ? 0 : value;
    }

    public static float lerp(float start, float end, float factor) {
        return start + (end - start) * factor;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }
}