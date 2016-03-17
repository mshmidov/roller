package com.mshmidov.roller.function;

import com.google.common.collect.ImmutableList;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class TableRegistry {

    private static final Logger logger = LoggerFactory.getLogger(TableRegistry.class);

    private Map<String, Table<String>> tables = new HashMap<>();

    public Collection<Table<String>> getAllTables() {
        return ImmutableList.copyOf(tables.values());
    }

    public Optional<Table<String>> getTable(String name) {
        return Optional.ofNullable(tables.get(name));
    }

    public void putTable(Table<String> table) {
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

    public static TableRegistry newInstance() {
        final TableRegistry tableRegistry = new TableRegistry();

        discoverTables(new File("."), new TableLoader(new DiceExpressionParser())).forEach(tableRegistry::putTable);

        return tableRegistry;
    }

    private static Stream<Table> discoverTables(File directory, TableLoader tableLoader) {
        return FileUtils.listFiles(directory, new String[] { "table" }, true).stream()
                .map(tableLoader::loadTable)
                .flatMap(table -> table.map(Stream::of).orElse(Stream.empty()));
    }
}
