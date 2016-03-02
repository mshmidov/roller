package com.mshmidov.roller.model;

import static com.google.common.base.Preconditions.checkArgument;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public final class TableRegistry {

    private Map<String, Table> tables = new HashMap<>();

    public Optional<Table> getTable(String name) {
        return Optional.ofNullable(tables.get(name));
    }

    public void putTable(Table table) {
        final String name = table.getName();
        checkArgument(!getTable(name).isPresent(), "table named '" + name + "' is already registered");
        tables.put(name, table);
    }
}
