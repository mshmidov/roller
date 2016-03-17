package com.mshmidov.roller.function;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.mshmidov.roller.model.Range;
import it.unimi.dsi.util.XorShift128PlusRandom;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class RandomChoice {

    private static final XorShift128PlusRandom RANDOM = new XorShift128PlusRandom();

    private RandomChoice() {
    }

    public static <T> T from(T... options) {
        final int i = RANDOM.nextInt(options.length);
        return options[i];
    }

    public static <T> T from(List<T> options) {
        final int i = RANDOM.nextInt(options.size());
        return options.get(i);
    }

    public static <T extends Enum<T>> T from(Class<T> enumClass) {
        return from(enumClass.getEnumConstants());
    }

    public static int from(Range range) {
        return between(range.getLower(), range.getUpper());
    }

    public static <T> Stream<T> streamFrom(List<T> options) {
        final List<T> optionsCopy = ImmutableList.copyOf(options);
        return RANDOM.ints(0, optionsCopy.size()).mapToObj(optionsCopy::get);
    }

    public static int between(int lower, int higher) {
        checkArgument(lower < higher);
        return RANDOM.nextInt((higher - lower) + 1) + lower;
    }

    public static boolean byChance(double chance) {
        checkArgument(chance >= 0F && chance <= 1F);
        return RANDOM.nextDouble() >= 1 - chance;
    }

    public static <T> Optional<T> byChance(double chance, T value) {
        checkArgument(chance >= 0F && chance <= 1F);
        return RANDOM.nextDouble() >= 1 - chance ? Optional.of(value) : Optional.empty();
    }
}
