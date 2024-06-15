package ru.leo.stats;

import java.util.List;
import java.util.Map;

public interface IStats {
    void addStat(String tag, long value);

    Map<String, Long> getStatAvg();

    Map<String, List<Long>> getStats();
}
