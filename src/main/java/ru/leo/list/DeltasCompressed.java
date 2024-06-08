package ru.leo.list;

import java.util.BitSet;

public class DeltasCompressed implements IDeltasCompressed {
    private final int deltaBits;
    private final int realBitsSize;
    private final BitSet deltasBs;


    public DeltasCompressed(int deltaBits, int realBitsSize, BitSet deltasBs) {
        this.deltaBits = deltaBits;
        this.realBitsSize = realBitsSize;
        this.deltasBs = deltasBs;
    }

    @Override
    public BitSet get(int i) {
        BitSet result = new BitSet(deltaBits);
        for (int bI = 0; bI < deltaBits; bI++) {
            if (deltasBs.get(getCurrentDeltaI(i, bI))) {
                result.set(bI);
            }
        }

        return result;
    }

    private int getCurrentDeltaI(int current, int index) {
        if (index >= deltaBits) {
            throw new IllegalArgumentException();
        }

        return current * deltaBits + index;
    }

//    private int current;
//
//    @Override
//    public boolean hasNext() {
//        return getCurrentDeltaI(0) < realBitsSize;
//    }
//
//    @Override
//    public BitSet next() {
//        if (!hasNext()) {
//            throw new NoSuchElementException();
//        }
//
//        // TODO: Неэффективно, а что делать
//        BitSet result = new BitSet(deltaBits);
//        for (int i = 0; i < deltaBits; i++) {
//            if (deltasBs.get(getCurrentDeltaI(i))) {
//                result.set(i);
//            }
//        }
//
//        current++;
//        return result;
//    }
//
//    private int getCurrentDeltaI(int index) {
//        if (index >= deltaBits) {
//            throw new IllegalArgumentException();
//        }
//
//        return current * deltaBits + index;
//    }
}
