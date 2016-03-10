package com.mshmidov.roller.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Range implements Iterable<Integer> {

    private final int lower;

    private final int upper;

    public Range(int singular) {
        this(singular, singular);
    }

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

    public int size() {
        return upper - lower + 1;
    }

    public boolean contains(int value) {
        return (lower <= value) && (value <= upper);
    }

    public boolean contains(Range range) {
        return contains(range.lower) && contains(range.upper);
    }

    public boolean intersects(Range range) {
        return contains(range.lower) || contains(range.upper);
    }

    @Override
    public Iterator<Integer> iterator() {
        return IntStream.range(lower, upper + 1).iterator();
    }

    public Stream<Integer> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Range)) {
            return false;
        }

        final Range other = (Range) o;
        return lower == other.lower &&
                upper == other.upper;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lower, upper);
    }

    @Override
    public String toString() {
        return (lower != upper)
                ? lower + ".." + upper
                : String.valueOf(lower);
    }
}
