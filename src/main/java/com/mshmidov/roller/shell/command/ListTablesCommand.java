package com.mshmidov.roller.shell.command;

import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.model.TableRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ListTablesCommand implements CommandMarker {

    @Autowired private TableRegistry tableRegistry;

    @CliAvailabilityIndicator(value = "list tables")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "list tables", help = "lists all registered tables")
    public Collection<String> execute() {
        return tableRegistry.getAllTables().stream().map(Table::getName).collect(Collectors.toList());
    }
}
