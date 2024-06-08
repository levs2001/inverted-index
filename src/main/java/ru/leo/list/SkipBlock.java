package ru.leo.list;

public record SkipBlock(long firstId, int excFirstInd) {
    public static SkipBlock create(long firstId, int excFirstInd) {
        return new SkipBlock(firstId, excFirstInd);
    }
}
