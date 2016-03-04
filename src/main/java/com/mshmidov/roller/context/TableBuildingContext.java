package com.mshmidov.roller.context;

import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.model.TableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class TableBuildingContext implements InteractiveContext<Table> {

    private enum RowMode {
        BY_ORDER,
        BY_RANGE
    }


    private static final Logger logger = LoggerFactory.getLogger(TableBuildingContext.class);
    private final String name;

    private final TableRegistry tableRegistry;

    private final Map<Integer, String> rows = new HashMap<>();

    private boolean erroneous = false;
    private Optional<RowMode> rowMode = Optional.empty();

    private int nextRow = 1;

    public TableBuildingContext(String name, TableRegistry tableRegistry) {
        this.name = name;
        this.tableRegistry = tableRegistry;
    }

    @Override
    public String getPrompt() {
        return (erroneous)
                ? "will not build table"
                : String.format("building table '%s'", name);
    }

    @Override
    public Optional<Table> done() {
        if (erroneous) {
            logger.warn("Table was not created");
            return Optional.empty();
        } else {
            final Table table = new Table(name);
            tableRegistry.putTable(table);
            return Optional.of(table);
        }
    }

    public void setErroneous(boolean erroneous) {
        this.erroneous = erroneous;
    }

    public void addRow(Optional<Range> range, String value) {
        if (range.isPresent()) {
            addRow(range.get(), value);
        } else {
            addRow(value);
        }
    }

    public void addRow(String value) {
        if (!rowMode.isPresent()) {
            rowMode = Optional.of(RowMode.BY_ORDER);
        }

        if (rowMode.map(RowMode.BY_ORDER::equals).orElse(false)) {
            rows.put(nextRow++, value);

        } else {
            logger.warn("Ths and further rows should be added by range. Row will not be added.");
        }
    }

    public void addRow(Range range, String value) {
        if (!rowMode.isPresent()) {
            rowMode = Optional.of(RowMode.BY_RANGE);
        }

        if (rowMode.map(RowMode.BY_RANGE::equals).orElse(false)) {

            final boolean intersects = range.stream().map(rows::containsKey).anyMatch(Boolean.TRUE::equals);

            if (intersects) {
                logger.warn("Row range intersects with already existing row(s). Row will not be added.");
            } else {
                range.forEach(i -> rows.put(i, value));
            }

        } else {
            logger.warn("This and further rows should be added by order. Row will not be added.");
        }
    }
}
