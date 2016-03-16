package com.mshmidov.roller.shell.command.supplier;

import com.mshmidov.roller.core.function.Rendering;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.shell.command.AbstractCommand;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class TableShowCommand extends AbstractCommand {

    @CliCommand(value = "show table", help = "displays a table")
    public String execute(@CliOption(key = { "", "name" }, mandatory = true, help = "Name of the table to show") Table table) {
        return Rendering.renderTable(table);
    }
}
