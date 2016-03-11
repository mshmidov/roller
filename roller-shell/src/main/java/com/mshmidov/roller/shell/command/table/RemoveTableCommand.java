package com.mshmidov.roller.shell.command.table;

import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class RemoveTableCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(RemoveTableCommand.class);


    @Autowired TableRegistry tableRegistry;

    @CliAvailabilityIndicator(value = "remove table")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "remove table", help = "removes registered table")
    public void execute(@CliOption(key = { "", "name" }, mandatory = true, help = "Name of the table to remove") Table table) {
        if (tableRegistry.removeTable(table.getName())) {
            logger.info("Table removed");
        } else {
            logger.info("No such table");
        }
    }
}
