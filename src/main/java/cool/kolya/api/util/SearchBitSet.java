package cool.kolya.api.util;

import java.util.Arrays;
import java.util.BitSet;

/**
 * компромисс между памятью и поиском первых(пока что) set/clear битов
 * при достаточно большом размере битсета
 */
public class SearchBitSet {

    private static final long LONG_SHIFT = 6; //power of two
    private static final int  DEFAULT_WORD_CAPACITY = 2;
    private static final int  NEGATIVE_BIT = 1 << 31;
    private static final long FULL_SET_WORD = -1;
    private static final long FULL_CLEAR_WORD = 0;

    private long[] words;
    private final BitSet fullSetWordsBitSet;
    private final BitSet notClearWordsBitSet;

    public SearchBitSet(int capacity) {
        int wordsSize = wordIndex(capacity) + 1;
        words = new long[wordsSize];
        fullSetWordsBitSet = new BitSet(wordsSize);
        notClearWordsBitSet = new BitSet(wordsSize);
    }

    public SearchBitSet() {
        words = new long[DEFAULT_WORD_CAPACITY];
        fullSetWordsBitSet = new BitSet(DEFAULT_WORD_CAPACITY);
        notClearWordsBitSet = new BitSet(DEFAULT_WORD_CAPACITY);
    }

    public void set(int bitOffset) {
        checkBitOffset(bitOffset);
        int wordIndex = wordIndex(bitOffset);
        ensureCapacityIfNeed(wordIndex);
        if ((words[wordIndex] |= 1L << bitOffset) == FULL_SET_WORD) {
            //freeWords &= ~(1L << wordIndex);
            fullSetWordsBitSet.set(wordIndex);
        } else if (!notClearWordsBitSet.get(wordIndex)) {
            notClearWordsBitSet.set(wordIndex);
        }
    }

    public void clearIndex(int bitOffset) {
        int wordIndex = wordIndex(bitOffset);
        ensureCapacityIfNeed(wordIndex);
        words[wordIndex] &= ~(1L << bitOffset);
        if (fullSetWordsBitSet.get(wordIndex)) {
            //freeWords |= 1L << wordIndexOffset;
            fullSetWordsBitSet.clear(wordIndex);
        } else if (words[wordIndex] == FULL_CLEAR_WORD) {
            notClearWordsBitSet.clear(wordIndex);
        }
    }

    public boolean get(int bitOffset) {
        int wordIndex = wordIndex(bitOffset);
        return (words[wordIndex] & (1L << bitOffset)) == 1;
    }

    public int firstClear() {
        int wordIndex = fullSetWordsBitSet.nextClearBit(0);
        int bitOffset = wordIndex < words.length
                ? Long.numberOfTrailingZeros(~words[wordIndex])
                : 0;
        return bitOffset + (wordIndex << LONG_SHIFT);
    }

    public int firstSet() {
        int wordIndex = notClearWordsBitSet.nextSetBit(0);
        if (wordIndex == -1) {
            return -1;
        }
        return Long.numberOfTrailingZeros(words[wordIndex]) + (wordIndex << LONG_SHIFT);
    }

    private int wordIndex(int bitOffset) {
        return bitOffset >> LONG_SHIFT;
    }

    private void checkBitOffset(int bitOffset) {
        if ((bitOffset & NEGATIVE_BIT) == NEGATIVE_BIT) {
            throw new IllegalArgumentException("bit offset can't be negative");
        }
    }

    private void ensureCapacityIfNeed(int wordIndex) {
        if ((words.length) <= wordIndex) {
            words = Arrays.copyOf(words, Math.max(2 * words.length, wordIndex));
        }
    }
}