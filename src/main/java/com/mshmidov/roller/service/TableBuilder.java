package com.mshmidov.roller.service;

import static java.util.Comparator.comparingInt;

import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.TreeMap;

final class TableBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TableBuilder.class);

    private final String name;

    private final TreeMap<Range, String> rows = new TreeMap<>(comparingInt(Range::getLower));

    private boolean erroneous = false;

    public TableBuilder(String name) {
        this.name = name;
    }

    public void addRow(Optional<Range> range, String row) {

        final Range effectiveRange = range.orElseGet(() -> new Range(rows.isEmpty() ? 1 : rows.lastKey().getUpper() + 1));

        final boolean intersects = rows.keySet().stream().anyMatch(effectiveRange::intersects);

        if (intersects) {
            logger.warn("Row range intersects with already existing row(s). Row will not be added.");
            erroneous = true;

        } else {
            rows.put(effectiveRange, row);
        }

    }

    public Optional<Table> build(Optional<DiceExpression> dice) {

        if (rows.isEmpty()) {
            logger.warn("Cannot create table without rows.");

        } else if (!erroneous) {
            try {
                return Optional.of(new Table(name, rows, dice));

            } catch (IllegalArgumentException e) {
                logger.warn(e.getMessage());
            }
        }

        return Optional.empty();
    }

}
