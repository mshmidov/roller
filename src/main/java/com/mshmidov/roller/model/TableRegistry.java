package com.mshmidov.roller.model;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public final class TableRegistry {

    private Map<String, Table> tables = new HashMap<>();

    public Collection<Table> getAllTables() {
        return ImmutableList.copyOf(tables.values());
    }

    public Optional<Table> getTable(String name) {
        return Optional.ofNullable(tables.get(name));
    }

    public void putTable(Table table) {
        final String name = table.getName();
        checkArgument(!getTable(name).isPresent(), "table named '" + name + "' is already registered");
        tables.put(name, table);
    }
}
