package com.mshmidov.roller.model;

import com.google.common.base.Preconditions;
import com.mshmidov.roller.function.Bounded;
import com.mshmidov.roller.function.RollDice;
import com.wandrell.tabletop.dice.notation.DiceExpression;

import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public final class Table {

    private final String name;
    private final TreeMap<Integer, String> rows;
    private final Optional<DiceExpression> roll;
    private final Bounded bounds;
    private final Supplier<Integer> randomRow;

    public Table(String name, TreeMap<Integer, String> rows, Optional<DiceExpression> roll) {
        Preconditions.checkArgument(checkContinuity(rows), "Rows should be continuously numbered");
        this.name = name;
        this.rows = new TreeMap<>(rows);
        this.roll = roll;
        this.bounds = Bounded.with(rows.firstKey(), rows.lastKey());
        this.randomRow = () -> ThreadLocalRandom.current().nextInt(rows.firstKey(), rows.lastKey() + 1);
    }

    public String getName() {
        return name;
    }

    public TreeMap<Integer, String> getRows() {
        return new TreeMap<>(rows);
    }

    public Supplier<Integer> getRoll() {
        return roll.map(RollDice::rollDice)
                .orElse(randomRow);
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
