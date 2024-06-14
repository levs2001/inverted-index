package ru.leo.list;

import java.util.BitSet;
import java.util.NoSuchElementException;

public class PostingList implements IPostingList {
    private static final long NOT_INIT = -1;
    private final DocIdList docIdList;
    private long currentId;
    private int nextInd;
    private int excInd;
    private int skipBlockInd;

    public PostingList(DocIdList docIdList) {
        this.docIdList = docIdList;
        this.currentId = NOT_INIT;
    }

    @Override
    public boolean hasNext() {
        return nextInd < docIdList.size();
    }

    @Override
    public long next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (nextInd == 0) {
            currentId = docIdList.skipBlocks().getFirst().firstId();
            nextInd++;
            return currentId;
        }

        BitSet deltaBs = docIdList.deltasCompressed().get(nextInd - 1);
        Long delta = docIdList.deltaCodeMap().get(deltaBs);
        if (delta == null) {
            delta = docIdList.exceptions().get(excInd);
            excInd++;
        }

        currentId += delta;

        nextInd++;
        return currentId;
    }

    @Override
    public long current() {
        return currentId;
    }

    @Override
    public long advance(long docId) {
        boolean skipped = false;
        int curInd = skipBlockInd;
        while (curInd < docIdList.skipBlocks().size()) {
            if (docIdList.skipBlocks().get(curInd).firstId() > docId) {
                break;
            }
            skipped = true;
            curInd++;
        }

        if (skipped) {
            skipBlockInd = curInd - 1;
            SkipBlock skipBlock = docIdList.skipBlocks().get(skipBlockInd);
            currentId = skipBlock.firstId();
            excInd = skipBlock.excFirstInd();
        }

        long result = currentId;
        while (result < docId && hasNext()) {
            result = next();
        }
        return result;
    }

    @Override
    public int size() {
        return docIdList.size();
    }

    @Override
    public void reset() {
        currentId = 0;
        nextInd = 0;
        excInd = 0;
        skipBlockInd = 0;
    }
}
