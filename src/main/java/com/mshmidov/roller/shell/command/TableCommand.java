package com.mshmidov.roller.shell.command;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.context.TableBuildingContext;
import com.mshmidov.roller.model.TableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class TableCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(TableCommand.class);

    @Autowired CurrentContext context;

    @Autowired TableRegistry tableRegistry;

    @CliAvailabilityIndicator(value = "table")
    public boolean isAvailable() {
        return !context.getInteractiveContext().isPresent();
    }

    @CliCommand(value = "table", help = "enters table creation context")
    public void execute(@CliOption(key = { "", "name" }, mandatory = true, help = "Unique table name") final String name) {
        final TableBuildingContext newContext = new TableBuildingContext(name, tableRegistry);

        if (tableRegistry.getTable(name).isPresent()) {
            logger.warn("Table '" + name + "' is already registered and would not be changed");
            newContext.setErroneous(true);
        }

        context.startInteractiveContext(newContext);

    }
}
