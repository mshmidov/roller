package com.mshmidov.roller.shell.command.table;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.context.table.TableBuildingContext;
import com.mshmidov.roller.service.TableRegistry;
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

    public static final String KEYWORD = "table";

    @Autowired CurrentContext context;

    @Autowired TableRegistry tableRegistry;

    @CliAvailabilityIndicator(value = KEYWORD)
    public boolean isAvailable() {
        return !context.getInteractiveContext().isPresent();
    }

    @CliCommand(value = KEYWORD, help = "enters table creation context")
    public void execute(@CliOption(key = { "", "name" }, mandatory = true, help = "Unique table name") final String name) {
        context.startInteractiveContext(new TableBuildingContext(name, tableRegistry));
    }
}
