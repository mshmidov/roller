package com.mshmidov.roller.shell.command;

import com.mshmidov.roller.function.Functions;
import com.mshmidov.roller.model.Table;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class ShowTableCommand implements CommandMarker {

    @CliAvailabilityIndicator(value = "show table")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "show table", help = "displays a table")
    public String execute(@CliOption(key = { "" }, mandatory = true, help = "Name of the table to show") Table table) {
        return Functions.tableToString(table);
    }
}
