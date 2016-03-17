package com.mshmidov.roller.function;

import com.google.common.collect.ImmutableList;
import com.mshmidov.roller.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class TableRegistry {

    private static final Logger logger = LoggerFactory.getLogger(TableRegistry.class);

    private Map<String, Table> tables = new HashMap<>();

    public Collection<Table> getAllTables() {
        return ImmutableList.copyOf(tables.values());
    }

    public Optional<Table> getTable(String name) {
        return Optional.ofNullable(tables.get(name));
    }

    public void putTable(Table table) {
        final String name = table.getName();

        if (getTable(name).isPresent()) {
            logger.info("Table named '{}' is already registered", name);

        } else {
            tables.put(name, table);
            logger.info("Added table {}", table.toString());
        }
    }

    public boolean removeTable(String name) {
        return tables.remove(name) != null;
    }


}
