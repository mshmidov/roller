package com.mshmidov.roller.context;

import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.model.TableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TableBuildingContext implements InteractiveContext {

    private static final Logger logger = LoggerFactory.getLogger(TableBuildingContext.class);

    private final String name;
    private final TableRegistry tableRegistry;

    private boolean erroneous = false;

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

    public void setErroneous(boolean erroneous) {
        this.erroneous = erroneous;
    }

    @Override
    public void done() {
        if (erroneous) {
            logger.warn("Table was not created");
        } else {
            tableRegistry.putTable(new Table(name));
        }
    }
}
