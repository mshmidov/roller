package com.mshmidov.roller.shell.command.supplier;

import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableRegistry;
import com.mshmidov.roller.shell.command.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class TableRemoveCommand extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(TableRemoveCommand.class);

    @Autowired TableRegistry tableRegistry;

    @CliCommand(value = "remove table", help = "removes registered table")
    public void execute(@CliOption(key = { "", "name" }, mandatory = true, help = "Name of the table to remove") Table table) {
        if (tableRegistry.removeTable(table.getName())) {
            logger.info("Table removed");
        } else {
            logger.info("No such table");
        }
    }
}
