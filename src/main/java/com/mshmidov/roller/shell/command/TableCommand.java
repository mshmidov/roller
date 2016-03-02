package com.mshmidov.roller.shell.command;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.context.TableBuildingContext;
import com.mshmidov.roller.model.TableRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class TableCommand implements CommandMarker {

    @Autowired CurrentContext context;

    @Autowired TableRegistry tableRegistry;

    @CliAvailabilityIndicator(value = "table")
    public boolean isAvailable() {
        return !context.get().isPresent();
    }

    @CliCommand(value = "table", help = "enters table creation context")
    public String execute(@CliOption(key = { "", "name" }, mandatory = true, help = "Unique table name") final String name) {
        if (tableRegistry.getTable(name).isPresent()) {
            return "table '" + name + "' is already registered";
        } else {
            context.start(new TableBuildingContext(name, tableRegistry));
            return null;
        }
    }
}
