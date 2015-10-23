package rs.macro.api.util;

import rs.macro.api.util.fx.Shapes;

import java.awt.*;
import java.util.Collection;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Random {

    private static java.util.Random RANDOM = new java.util.Random();

    /**
     * Generates a random integer within specified range.
     *
     * @param min The minimum number to be generated.
     * @param max The maximum number to be generated.
     * @return The generated random integer.
     */
    public static int nextInt(int min, int max) {
        if (min > max) {
            int minT = min;
            min = max;
            max = minT;
        }
        if (min == max) {
            return min;
        }
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * Generates a random integer up to the specified maximum.
     *
     * @param max The maximum number to be generated.
     * @return The generated random integer.
     */
    public static int nextInt(int max) {
        return nextInt(0, max);
    }

    /**
     * Generates a random double within specified range.
     *
     * @param min The minimum number to be generated.
     * @param max The maximum number to be generated.
     * @return The generated random double.
     */
    public static double nextDouble(double min, double max) {
        if (min > max) {
            double minT = min;
            min = max;
            max = minT;
        }
        if (min == max) {
            return min;
        }
        return min + RANDOM.nextDouble() * (max - min);
    }

    /**
     * Generates a random float within specified range.
     *
     * @param min The minimum number to be generated
     * @param max The maximum number to be generated
     * @return The generated random float
     */
    public static float nextFloat(float min, float max) {
        if (min > max) {
            float minT = min;
            min = max;
            max = minT;
        }
        if (min == max) {
            return min;
        }
        return min + RANDOM.nextFloat() * (max - min);
    }

    /**
     * Gets a random integer based off the given parameters.
     *
     * @param min       The minimum number to be generated.
     * @param max       The maximum number to be generated.
     * @param average   The average.
     * @param deviation The standard deviation.
     * @return The generated random integer.
     */
    public static synchronized double nextGaussian(double min, double max,
                                                   double average, double deviation) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            double minT = min;
            min = max;
            max = minT;
        }
        double rand;
        do {
            rand = RANDOM.nextGaussian() * deviation + average;
        } while (rand < min || rand >= max);
        return rand;
    }

    /**
     * Gets a random integer based off the given parameters.
     *
     * @param min       The minimum number to be generated.
     * @param max       The maximum number to be generated.
     * @param deviation The standard deviation.
     * @return The generated random integer.
     */
    public static double nextGaussian(double min, double max, double deviation) {
        return nextGaussian(min, max, (max + min) / 2, deviation);
    }

    /**
     * Generates a random long.
     *
     * @param min The minimum number to be generated.
     * @param max The maximum number to be generated.
     * @return The generated random long.
     */
    public static long nextLong(long min, long max) {
        if (min > max) {
            long minT = min;
            min = max;
            max = minT;
        }
        if (min == max) {
            return min;
        }
        return (long) (RANDOM.nextDouble() * (max - min));
    }

    /**
     * Generates a random gaussian.
     *
     * @return The generated random integer.
     */
    public static double nextGaussian() {
        return RANDOM.nextGaussian();
    }

    /**
     * Generates a random boolean value.
     *
     * @return The generated random boolean.
     */
    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * Generates a random double.
     *
     * @return The generated random double.
     */
    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    /**
     * Generates a random integer.
     *
     * @return The generated random integer.
     */
    public static int nextInt() {
        return RANDOM.nextInt();
    }

    /**
     * Generates a random float.
     *
     * @return The generated random float.
     */
    public static float nextFloat() {
        return RANDOM.nextFloat();
    }

    /**
     * Generates a random long.
     *
     * @return The generated random long.
     */
    public static long nextLong() {
        return RANDOM.nextLong();
    }

    @SafeVarargs
    public static <T> T nextElement(T... elements) {
        return elements[nextInt(elements.length - 1)];
    }

    @SuppressWarnings("unchecked")
    public static <T> T nextElement(Collection<T> elements) {
        Object[] array = elements.toArray();
        return (T) nextElement(array);
    }

    public static Point nextPoint(Shape shape) {
        return nextElement(Shapes.pointsFor(shape));
    }
}