package com.mshmidov.roller.function;

import static java.util.Comparator.comparingInt;

import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.TreeMap;

public final class TableBuilder<T> {

    private static final Logger logger = LoggerFactory.getLogger(TableBuilder.class);

    private final String name;

    private final TreeMap<Range, T> rows = new TreeMap<>(comparingInt(Range::getLower));

    private boolean erroneous = false;

    public TableBuilder(String name) {
        this.name = name;
    }

    public TableBuilder<T> row(Range range, T row) {
        addRow(Optional.of(range), row);
        return this;
    }

    public TableBuilder<T> row(T row) {
        addRow(Optional.empty(), row);
        return this;
    }

    public void addRow(Optional<Range> range, T row) {

        final Range effectiveRange = range.orElseGet(() -> new Range(rows.isEmpty() ? 1 : rows.lastKey().getUpper() + 1));

        final boolean intersects = rows.keySet().stream().anyMatch(effectiveRange::intersects);

        if (intersects) {
            logger.error("Row range intersects with already existing row(s). Row will not be added.");
            erroneous = true;

        } else {
            rows.put(effectiveRange, row);
        }

    }

    public Optional<Table<T>> build(Optional<DiceExpression> dice) {

        if (rows.isEmpty()) {
            logger.error("Cannot create table without rows.");

        } else if (!erroneous) {
            try {
                return Optional.of(new Table<>(name, rows, dice));

            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());
            }
        }

        return Optional.empty();
    }

}
