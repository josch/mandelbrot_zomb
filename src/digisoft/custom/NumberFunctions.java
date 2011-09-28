package digisoft.custom;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2007/11/16
 */
public class NumberFunctions {

    private static final String ZEROES = "0000000000000000000000000000000000000000000000000000000000000000";
    private static SecureRandom secureRnd = null;
    public static final Random RND = new Random();
    public static final DecimalFormat COMMA_NUMBER = new DecimalFormat("#,###");

    public static String toStringFixedLength(int i, int len) {
        String out = Integer.toString(i);
        return NumberFunctions.ZEROES.substring(out.length(), len) + out;
    }

    public static String toStringFixedLength(long i, int len) {
        String out = Long.toString(i);
        return NumberFunctions.ZEROES.substring(out.length(), len) + out;
    }

    public static String toStringFixedLength(int i, int base, int len) {
        String out = Long.toString(i & 0xFFFFFFFFL, base);

        if (base > 10) {
            return NumberFunctions.ZEROES.substring(out.length(), len) + out.toUpperCase();
        }
        return NumberFunctions.ZEROES.substring(out.length(), len) + out;
    }

    public static String toStringFixedLength(long i, int base, int len) {
        String out = Long.toString(i, base);

        if (base > 10) {
            return NumberFunctions.ZEROES.substring(out.length(), len) + out.toUpperCase();
        }
        return NumberFunctions.ZEROES.substring(out.length(), len) + out;
    }

    public static Random getSecureRandom() {
        if (NumberFunctions.secureRnd == null) {
            NumberFunctions.secureRnd = new SecureRandom();
        }
        return NumberFunctions.secureRnd;
    }

    /**
     * Find primes by recording a list with some proven non-prime numbers
     * (NPNs). If the NPNs are chosen carefully, the gaps between the non-prime
     * numbers will be prime numbers. <br>
     * The NPNs are chosen by calculating rectangles with an area which is an
     * odd number. The length is stepped through every odd number in the range [
     * <tt>3</tt>, <tt>sqrt(guess)</tt>]. For every length, the height is
     * stepped through such numbers (also odd numbers) so the area will stay
     * within the range [<tt>guess-gapSize</tt>, <tt>guess</tt>). When the
     * largest gap between any two successive successive prime numbers p1 and p2
     * (gap=|p2-p1|) lower than <tt>guess</tt> is smaller or equal to
     * <tt>gapSize</tt>, the result will be a prime number deterministically.<br>
     * <br>
     * Warning: Do not use this routine as-is for finding a random prime number
     * p<sup>n</sup> for any guess with equal distribution of n. The difference
     * in gaps between successive prime numbers causes the distribution to bias
     * greatly. Use <tt>findRandomPrime</tt> instead.
     * 
     * @param guess
     *            an initial guess
     * @return the largest prime smaller than or equal to guess
     * @since 1.0
     * @see NumberFunctions#findRandomPrime(int, int)
     * @see http://www.trnicely.net/
     */
    public static int findPrimeNear(int guess) {
        // Make it an odd number.
        if ((guess & 1) == 0) {
            guess--;
        }

        // Find largest gap
        int maxGap;
        if (guess <= 523) {
            // Minimum gap for <7 bits. (closest was 9.030667136 bits)
            maxGap = 14;
        } else if (guess <= 155921) {
            // Minimum gap for <15 bits. (closest was 17.25045573 bits)
            maxGap = 72;
        } else if (guess <= 2147437599) // Magic number? see else block.
        {
            // Minimum gap for <31 bits. (closest was 31.09957781 bits)
            maxGap = 292;
        } else {
            throw new IllegalArgumentException("guess+floor(sqrt(guess))-292 should be below 2^31-1, so guess should be below 2147437600");
        }
        int halfGap = maxGap >> 1;

        // The list is compressed and reversed. (ie. index 0 is the guess, 1 is
        // guess-2, etc.)
        boolean[] primes = new boolean[halfGap];
        Arrays.fill(primes, true);

        int halfway = (int) StrictMath.sqrt(guess);
        int minGap = guess - maxGap;

        // fill the array with nonprimes
        for (int i = 3; i <= halfway; i += 2) {
            int max = guess / i;
            int min = (minGap + i) / i;
            if ((min & 1) == 0) {
                min++;
            }
            for (int j = min; j <= max; j += 2) {
                int nonPrime = j * i;
                int index = guess - nonPrime >> 1;
                if (index >= 0) {
                    primes[index] = false;
                }
            }
        }

        // Find the highest prime number in the list.
        for (int a = 0; a < halfGap; a++) {
            if (primes[a]) {
                return guess - (a << 1);
            }
        }
        throw new IllegalStateException("gap too small");
    }

    /**
     * Find primes by recording a list with some proven non-prime numbers
     * (NPNs). If the NPNs are chosen carefully, the gaps between the non-prime
     * numbers will be prime numbers. <br>
     * The NPNs are chosen by calculating rectangles with an area which is an
     * odd number. The length is stepped through every odd number in the range [
     * <tt>3</tt>, <tt>sqrt(guess)</tt>]. For every length, the height is
     * stepped through such numbers (also odd numbers) so the area will stay
     * within the range [<tt>guess-gapSize</tt>, <tt>guess</tt>). When the
     * largest gap between any two successive successive prime numbers p1 and p2
     * (gap=|p2-p1|) lower than <tt>guess</tt> is smaller or equal to
     * <tt>gapSize</tt>, the result will be a prime number deterministically.<br>
     * <br>
     * Warning: Do not use this routine as-is for finding a random prime number
     * p<sup>n</sup> for any guess with equal distribution of n. The difference
     * in gaps between successive prime numbers causes the distribution to bias
     * greatly. Use <tt>findRandomPrime</tt> instead.
     * 
     * @param guess
     *            an initial guess
     * @return the largest prime smaller than or equal to guess
     * @since 1.0
     * @see NumberFunctions#findRandomPrime(int, int)
     * @see http://www.trnicely.net/
     */
    public static long findPrimeNear(long guess) {
        // Make it an odd number.
        if ((guess & 1) == 0) {
            guess--;
        }

        // Find largest gap
        int maxGap;
        if (guess <= 2147437600) {// Delegate to a faster function.
            return NumberFunctions.findPrimeNear((int) guess);
        } else if (guess <= 2300942549L) {
            // Minimum gap for <31 bits. (closest was 31.10 bits)
            maxGap = 292;
        } else if (guess <= 9223372033817776756L) // Magic number? see else block.
        {
            // There is known gap yet for <63 bits, so I'm using the next known
            // gap (85.90 bits)
            maxGap = 1448;
        } else {
            throw new IllegalArgumentException("guess+floor(sqrt(guess))-1448 should be below 2^63-1, so guess should be below 9223372033817776757");
        }
        int halfGap = maxGap >> 1;

        // The list is compressed and reversed. (ie. index 0 is the guess, 1 is
        // guess-2, etc.)
        boolean[] primes = new boolean[halfGap];
        Arrays.fill(primes, true);

        long halfway = (long) StrictMath.sqrt(guess);
        long minGap = guess - maxGap;

        // fill the array with nonprimes
        for (long i = 3; i <= halfway; i += 2) {
            long max = guess / i;
            long min = (minGap + i) / i;
            if ((min & 1) == 0) {
                min++;
            }
            for (long j = min; j <= max; j += 2) {
                long nonPrime = j * i;
                int index = (int) (guess - nonPrime) >> 1;
                if (index >= 0) {
                    primes[index] = false;
                }
            }
        }

        // Find the highest prime number in the list.
        for (int a = 0; a < halfGap; a++) {
            if (primes[a]) {
                return guess - (a << 1);
            }
        }
        throw new IllegalStateException("gap too small");
    }

    public static int factorial(int v) {
        int out = 1;
        for (int i = 2; i <= v; i++) {
            out *= i;
        }
        return out;
    }
}
