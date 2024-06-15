package ru.leo.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.leo.util.MathUtils;

/** Not thread safe! **/
public class StatsImpl implements IStats {
    private final Map<String, List<Long>> stats;

    public StatsImpl() {
        stats = new HashMap<>();
    }

    @Override
    public void addStat(String tag, long value) {
        stats.computeIfAbsent(tag, t -> new ArrayList<>());
        stats.get(tag).add(value);
    }

    @Override
    public Map<String, Long> getStatAvg() {
        Map<String, Long> result = new HashMap<>();
        for (String tag : stats.keySet()) {
            result.put(tag, MathUtils.avg(stats.get(tag)));
        }
        return result;
    }

    @Override
    public Map<String, List<Long>> getStats() {
        return stats;
    }
}
