package com.mshmidov.roller.function;

import java.util.function.Function;

public final class Bounded implements Function<Integer, Integer> {

    public final int min;

    public final int max;

    public Bounded(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer apply(Integer value) {
        return Math.min(Math.max(value, min), max);
    }

    public static Bounded with(int min, int max) {
        return new Bounded(min, max);
    }
}
