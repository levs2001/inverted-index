package ru.leo.util;

import org.junit.jupiter.api.Test;

class MathUtilsTest {
    @Test
    public void testBitSetMaker() {
        for (int i = 0; i < 10; i++) {
            System.out.println(MathUtils.bitsFromInt(4, i));
        }
    }
}