package com.mshmidov.roller.model;

import com.google.common.base.Preconditions;
import com.mshmidov.roller.function.Functions;
import com.mshmidov.roller.function.IntToIntFunction;
import com.wandrell.tabletop.dice.notation.DiceExpression;

import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class Table {

    private final String name;
    private final TreeMap<Integer, String> rows;
    private final IntSupplier roll;
    private final IntToIntFunction bounds;

    public Table(String name, TreeMap<Integer, String> rows, Optional<DiceExpression> roll) {
        Preconditions.checkArgument(checkContinuity(rows), "Rows should be continuously numbered");
        this.name = name;
        this.rows = new TreeMap<>(rows);
        this.bounds = Functions.valueInBounds(rows.firstKey(), rows.lastKey());
        this.roll = roll
                .map(Functions::diceRollSupplier)
                .orElse(() -> ThreadLocalRandom.current().nextInt(rows.firstKey(), rows.lastKey() + 1));
    }

    public String getName() {
        return name;
    }

    public TreeMap<Integer, String> getRows() {
        return new TreeMap<>(rows);
    }

    public IntSupplier getRoll() {
        return roll;
    }

    public String getValue(int index) {
        return rows.get(bounds.apply(index));
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean checkContinuity(TreeMap<Integer, String> rows) {
        return rows.lastKey() - rows.firstKey() == rows.size() - 1;
    }
}
