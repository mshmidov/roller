package com.mshmidov.roller.context.table;

import static java.util.Comparator.comparingInt;

import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public final class TableBuilderByOrder implements TableBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TableBuilderByOrder.class);

    private final String name;

    private final TreeMap<Integer, String> rows = new TreeMap<>();
    private final TreeMap<Range, String> groupedRows = new TreeMap<>(comparingInt(Range::getLower));

    private int lastKey = 1;

    public TableBuilderByOrder(String name) {
        this.name = name;
    }

    @Override
    public void addRow(Optional<Range> range, String value) {

        if (range.isPresent()) {
            logger.warn("This and further rows should be added by order. Row will not be added.");

        } else {
            rows.put(lastKey, value);
            groupedRows.put(new Range(lastKey, lastKey), value);
            lastKey++;
        }
    }

    @Override
    public NavigableMap<Integer, String> getRows() {
        return Collections.unmodifiableNavigableMap(rows);
    }

    @Override
    public Optional<Table> build(Optional<DiceExpression> roll) {

        if (rows.isEmpty()) {
            logger.warn("Cannot create table without rows.");
            return Optional.empty();
        }

        return Optional.of(new Table(name, rows, groupedRows, roll));
    }

}
