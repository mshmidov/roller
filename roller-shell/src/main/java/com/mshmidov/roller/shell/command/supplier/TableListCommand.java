package com.mshmidov.roller.shell.command.supplier;

import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableRegistry;
import com.mshmidov.roller.shell.command.AbstractCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TableListCommand extends AbstractCommand {

    @Autowired private TableRegistry tableRegistry;

    @CliCommand(value = "list tables", help = "lists all registered tables")
    public Collection<Table> execute() {
        return tableRegistry.getAllTables();
    }
}
