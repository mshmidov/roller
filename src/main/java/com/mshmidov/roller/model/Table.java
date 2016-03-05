package com.mshmidov.roller.model;

import com.google.common.base.Preconditions;
import com.mshmidov.roller.function.Functions;
import com.mshmidov.roller.function.IntToIntFunction;
import com.wandrell.tabletop.dice.notation.DiceExpression;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;

public final class Table {

    private final String name;
    private final TreeMap<Integer, String> rows;
    private final TreeMap<Range, String> groupedRows;
    private final IntSupplier roll;
    private final IntToIntFunction bounds;
    private final String description;

    public Table(String name, TreeMap<Integer, String> rows, TreeMap<Range, String> groupedRows, Optional<DiceExpression> roll) {
        this.groupedRows = groupedRows;
        Preconditions.checkArgument(checkContinuity(rows), "Rows should be continuously numbered");
        this.name = name;
        this.rows = new TreeMap<>(rows);
        this.bounds = Functions.valueInBounds(rows.firstKey(), rows.lastKey());
        this.roll = roll
                .map(Functions::diceRollSupplier)
                .orElse(() -> ThreadLocalRandom.current().nextInt(rows.firstKey(), rows.lastKey() + 1));

        this.description = String.format("%s (%s) %d rows, %d choices",
                name,
                roll.map(DiceExpression::getPrintableText).orElse(String.format("%d..%d", rows.firstKey(), rows.lastKey())),
                rows.size(),
                rows.values().stream().distinct().count());
    }

    public String getName() {
        return name;
    }

    public NavigableMap<Integer, String> getRows() {
        return Collections.unmodifiableNavigableMap(rows);
    }

    public NavigableMap<Range, String> getGroupedRows() {
        return Collections.unmodifiableNavigableMap(groupedRows);
    }

    public IntSupplier getRoll() {
        return roll;
    }

    public String getValue(int index) {
        return rows.get(bounds.apply(index));
    }

    @Override
    public String toString() {
        return description;
    }

    public static boolean checkContinuity(TreeMap<Integer, String> rows) {
        return rows.lastKey() - rows.firstKey() == rows.size() - 1;
    }
}
