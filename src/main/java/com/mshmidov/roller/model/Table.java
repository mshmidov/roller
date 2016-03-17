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

public final class Table {

    private final String name;
    private final TreeMap<Range, String> rows;
    private final TreeMap<Integer, String> rowIndex;
    private final IntSupplier roll;

    private final IntToIntFunction bounds;
    private final String description;

    public Table(String name, TreeMap<Range, String> rows, Optional<DiceExpression> roll) {
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

    public NavigableMap<Range, String> getRows() {
        return Collections.unmodifiableNavigableMap(rows);
    }

    public IntSupplier getRoll() {
        return roll;
    }

    public String getValue(int index) {
        return rowIndex.get(bounds.apply(index));
    }

    public String rollValue() {
        return getValue(getRoll().getAsInt());
    }

    @Override
    public String toString() {
        return description;
    }

    public static boolean checkContinuity(TreeMap<Integer, String> rows) {
        return rows.lastKey() - rows.firstKey() == rows.size() - 1;
    }
}
