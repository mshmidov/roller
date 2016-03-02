package com.mshmidov.roller.context;

import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.model.TableRegistry;

public final class TableBuildingContext implements InteractiveContext {

    private final String name;
    private final TableRegistry tableRegistry;

    public TableBuildingContext(String name, TableRegistry tableRegistry) {
        this.name = name;
        this.tableRegistry = tableRegistry;
    }

    @Override
    public String getPrompt() {
        return String.format("building table '%s'", name);
    }

    public String getName() {
        return name;
    }

    @Override
    public void done() {
        tableRegistry.putTable(new Table(name));
    }
}
