package com.mshmidov.roller.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.stream.IntStream;

public final class Range implements Iterable<Integer> {

    private final int lower;

    private final int upper;

    public Range(int lower, int upper) {
        Preconditions.checkArgument(lower <= upper, String.format("%s..%s is an invalid range", lower, upper));
        this.lower = lower;
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }

    @Override
    public Iterator<Integer> iterator() {
        return IntStream.range(lower, upper+1).iterator();
    }

    @Override
    public String toString() {
        return lower + ".." + upper;
    }
}
