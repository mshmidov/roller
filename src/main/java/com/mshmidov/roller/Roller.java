package com.mshmidov.roller;

import com.mshmidov.roller.function.TableLoader;
import com.mshmidov.roller.function.TableRegistry;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.stream.Stream;

public class Roller {

    public static void main(String[] args) {
        new Roller().createTableRegistry().getAllTables().forEach(table -> System.out.println(table.getName()));
    }

    protected TableRegistry createTableRegistry() {
        final TableRegistry tableRegistry = new TableRegistry();

        discoverTables(new File("."), new TableLoader(new DiceExpressionParser())).forEach(tableRegistry::putTable);

        return tableRegistry;
    }

    private Stream<Table> discoverTables(File directory, TableLoader tableLoader) {
        return FileUtils.listFiles(directory, new String[] { "table" }, true).stream()
                .map(tableLoader::loadTable)
                .flatMap(table -> table.map(Stream::of).orElse(Stream.empty()));
    }
}
