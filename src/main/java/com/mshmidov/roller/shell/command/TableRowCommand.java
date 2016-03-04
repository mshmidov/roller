package com.mshmidov.roller.shell.command;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.context.table.TableBuildingContext;
import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.TableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TableRowCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(TableRowCommand.class);

    @Autowired CurrentContext context;

    @Autowired TableRegistry tableRegistry;

    @CliAvailabilityIndicator(value = "row")
    public boolean isAvailable() {
        return context.getInteractiveContext().map(c -> c instanceof TableBuildingContext).orElse(false);
    }

    @CliCommand(value = "row", help = "adds a row to a table that is currently being created")
    public void execute(
            @CliOption(key = { "on" }, mandatory = false, help = "range of table roll results") final Range range,
            @CliOption(key = { "", "result" }, mandatory = true, help = "resulting value") final String value) {

        context.getInteractiveContext()
                .flatMap(c -> (c instanceof TableBuildingContext) ? Optional.of((TableBuildingContext) c) : Optional.empty())
                .ifPresent(builder -> builder.addRow(Optional.ofNullable(range), value));
    }
}
