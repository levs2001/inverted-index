package ru.leo.util;

import java.util.BitSet;
import java.util.List;

public class MathUtils {
    public static int log2(int x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x (" + x + ") must be positive");
        }

        return 32 - Integer.numberOfLeadingZeros(x - 1);
    }

    public static BitSet bitsFromInt(int bitsCount, int num) {
        if (bitsCount <= 0 || (num != 0 && bitsCount < log2(num))) {
            throw new IllegalArgumentException("Can't make bitset");
        }

        BitSet bitSet = new BitSet(bitsCount);
        int i = bitsCount - 1;
        while (num > 0) {
            if (num % 2 == 1) {
                bitSet.set(i);
            }
            num = num >> 1;
            i--;
        }

        return bitSet;
    }

    public static long avg(List<Long> list) {
        long sum = 0;
        for (long el : list) {
            sum += el;
        }
        return sum / list.size();
    }
}
