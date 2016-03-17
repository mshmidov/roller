package com.mshmidov.roller.model;


import com.google.common.base.Preconditions;
import com.mshmidov.roller.function.Functions;
import com.mshmidov.roller.function.IntToIntFunction;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public final class Table<T> {

    private final String name;
    private final TreeMap<Range, T> rows;
    private final TreeMap<Integer, T> rowIndex;
    private final IntSupplier roll;

    private final IntToIntFunction bounds;
    private final String description;

    public Table(String name, TreeMap<Range, T> rows, Optional<DiceExpression> roll) {
        this.name = name;
        this.rows = new TreeMap<>(rows);
        this.rowIndex = rows.entrySet().stream()
                .flatMap(entry -> entry.getKey().stream().map(i -> Pair.of(i, entry.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue,
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        TreeMap::new));

        Preconditions.checkArgument(checkContinuity(rowIndex), "Rows should be continuously numbered");

        this.roll = roll
                .map(Functions::diceRollSupplier)
                .orElse(() -> ThreadLocalRandom.current().nextInt(rowIndex.firstKey(), rowIndex.lastKey() + 1));

        this.bounds = Functions.valueInBounds(rowIndex.firstKey(), rowIndex.lastKey());
        this.description = String.format("%s (%s) %d rows, %d choices",
                name,
                roll.map(DiceExpression::getPrintableText).orElse(String.format("%d..%d", rowIndex.firstKey(), rowIndex.lastKey())),
                rows.size(),
                rows.values().stream().distinct().count());
    }

    public String getName() {
        return name;
    }

    public NavigableMap<Range, T> getRows() {
        return Collections.unmodifiableNavigableMap(rows);
    }

    public IntSupplier getRoll() {
        return roll;
    }

    public T getValue(int index) {
        return rowIndex.get(bounds.apply(index));
    }

    public T rollValue() {
        return getValue(getRoll().getAsInt());
    }

    public T rollValue(int modifier) {
        return getValue(getRoll().getAsInt() + modifier);
    }

    @Override
    public String toString() {
        return description;
    }

    public static boolean checkContinuity(TreeMap<Integer, ?> rows) {
        return rows.lastKey() - rows.firstKey() == rows.size() - 1;
    }
}
