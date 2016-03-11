package com.mshmidov.roller.shell.command.table;

import com.mshmidov.roller.shell.service.TableLoader;
import com.mshmidov.roller.shell.service.TableRegistry;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;
import java.util.stream.Stream;

@Component
public class DiscoverTablesCommand implements CommandMarker {

    @Autowired(required = false) private TableRegistry tableRegistry;

    @Autowired private TableLoader tableLoader;

    @CliAvailabilityIndicator(value = "discover tables")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "discover tables", help = "tries to recursively find and run all scripts named `*.table` starting from current directory")
    public void execute() {
        final Collection<File> tables = FileUtils.listFiles(new File("."), new String[] { "table" }, true);

        tables.stream()
                .map(tableLoader::loadTable)
                .flatMap(table -> table.map(Stream::of).orElse(Stream.empty()))
                .forEach(tableRegistry::putTable);
    }


}
