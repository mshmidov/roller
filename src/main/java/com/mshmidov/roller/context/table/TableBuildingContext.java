package com.mshmidov.roller.context.table;

import com.mshmidov.roller.context.InteractiveContext;
import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.model.TableRegistry;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class TableBuildingContext implements InteractiveContext<Table> {


    private static final Logger logger = LoggerFactory.getLogger(TableBuildingContext.class);

    private final String name;

    private final TableRegistry tableRegistry;

    private final boolean erroneous;

    private Optional<TableBuilder> tableBuilder = Optional.empty();
    private Optional<DiceExpression> roll = Optional.empty();

    public TableBuildingContext(String name, TableRegistry tableRegistry) {
        this.name = name;
        this.tableRegistry = tableRegistry;

        if (tableRegistry.getTable(name).isPresent()) {
            logger.warn("Table '" + name + "' is already registered and would not be changed");
            erroneous = true;
        } else {
            erroneous = false;
        }
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
            final Optional<Table> table = tableBuilder.flatMap(builder -> builder.build(roll));
            table.ifPresent(tableRegistry::putTable);
            return table;
        }
    }

    public void addRow(Optional<Range> range, String value) {
        if (!tableBuilder.isPresent()) {
            tableBuilder = (range.isPresent())
                    ? Optional.of(new TableBuilderByRange(name))
                    : Optional.of(new TableBuilderByOrder(name));
        }

        tableBuilder.ifPresent(builder -> builder.addRow(range, value));
    }

    public void setRoll(DiceExpression roll) {
        this.roll = Optional.of(roll);
    }
}
