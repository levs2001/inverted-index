package ru.leo.search.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.leo.util.MathUtils;

/**
 * @param size
 * @param skipBlocks - первые элементы скиплистов
 * @param deltasCompressed
 * @param deltaCodeMap
 * @param exceptions
 */
public record DocIdList(int size, List<SkipBlock> skipBlocks, IDeltasCompressed deltasCompressed,
                        Map<BitSet, Long> deltaCodeMap,
                        List<Long> exceptions) {
    private static final int SKIP_LIST_SIZE = 128;
    private static final long EXC_DELTA = -1;

    public IPostingList iterator() {
        return new PostingList(this);
    }

    public static DocIdList create(Collection<Long> ids) {
        if (ids.size() == 1) {
            return new DocIdList(
                1,
                List.of(SkipBlock.create(ids.stream().findFirst().get(), 0)),
                null, null, null
            );
        }

        List<Long> idsSorted = ids.stream().sorted().toList();

        long[] deltas = getDeltas(idsSorted);
        Set<Long> toCode = get80PercSet(deltas);
        // + 1 for delta exception value
        int deltaBits = MathUtils.log2(toCode.size() + 1);
        Map<Long, BitSet> codeMap = makeCodeMap(toCode, deltaBits);

        List<Long> exceptions = new ArrayList<>();
        List<SkipBlock> skipBlocks = new ArrayList<>(ids.size() / SKIP_LIST_SIZE + 1);
        int realBitsSize = deltaBits * deltas.length;
        BitSet deltasBs = new BitSet(realBitsSize);
        long curId = idsSorted.getFirst();
        for (int i = 0; i < deltas.length; i++) {
            if (i % SKIP_LIST_SIZE == 0) {
                skipBlocks.add(SkipBlock.create(curId, exceptions.size()));
            }

            long delta = deltas[i];
            curId += delta;
            BitSet bs;
            if (codeMap.get(delta) != null) {
                bs = codeMap.get(delta);
            } else {
                bs = codeMap.get(EXC_DELTA);
                exceptions.add(delta);
            }

            for (int bsI = 0; bsI < deltaBits; bsI++) {
                if (bs.get(bsI)) {
                    deltasBs.set(bsI + i * deltaBits);
                }
            }
        }

        IDeltasCompressed deltasCompressed = new DeltasCompressed(deltaBits, realBitsSize, deltasBs);
        Map<BitSet, Long> deltaCodeMap = new HashMap<>(codeMap.size());
        codeMap.keySet().stream().filter(k -> k != EXC_DELTA).forEach(k -> deltaCodeMap.put(codeMap.get(k), k));
        return new DocIdList(idsSorted.size(), skipBlocks, deltasCompressed, deltaCodeMap, exceptions);
    }

    private static Map<Long, BitSet> makeCodeMap(Set<Long> toCode, int bitsCount) {
        if (toCode.isEmpty()) {
            return Map.of(EXC_DELTA, MathUtils.bitsFromInt(1, 0));
        }

        Map<Long, BitSet> result = new HashMap<>();
        Long[] arr = toCode.toArray(new Long[0]);
        for (int i = 0; i < arr.length; i++) {
            BitSet bitSet = MathUtils.bitsFromInt(bitsCount, i);
            result.put(arr[i], bitSet);
        }
        result.put(EXC_DELTA, MathUtils.bitsFromInt(bitsCount, arr.length));

        return result;
    }

    private static Set<Long> get80PercSet(long[] arr) {
        long[] sorted = Arrays.copyOf(arr, arr.length);
        Arrays.sort(sorted);
        int perc80Size = (int) (sorted.length * 0.8);
        Set<Long> coded = new HashSet<>();
        for (int i = 0; i < perc80Size; i++) {
            coded.add(sorted[i]);
        }

        return coded;
    }

    private static long[] getDeltas(List<Long> sorted) {
        // Sorted have delta for first value too.
        long[] deltas = new long[sorted.size() - 1];
        for (int i = 0; i < sorted.size() - 1; i++) {
            long delta = sorted.get(i + 1) - sorted.get(i);
            deltas[i] = delta;
        }

        return deltas;
    }
}
