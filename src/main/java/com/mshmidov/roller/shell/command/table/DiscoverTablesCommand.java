package com.mshmidov.roller.shell.command.table;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.function.TableValidator;
import com.mshmidov.roller.shell.RollerJLineShellComponent;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.commands.ScriptCommands;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

@Component
public class DiscoverTablesCommand implements CommandMarker {

    @Autowired(required = false) RollerJLineShellComponent shell;

    @Autowired private CurrentContext currentContext;

    @Autowired(required = false) private ScriptCommands scriptCommands;

    @CliAvailabilityIndicator(value = "discover tables")
    public boolean isAvailable() {
        return !currentContext.getInteractiveContext().isPresent();
    }

    @CliCommand(value = "discover tables", help = "tries to recursively find and run all scripts named `*.table` starting from current directory")
    public void execute() {
        final Collection<File> tables = FileUtils.listFiles(new File("."), new String[] { "table" }, true);

        tables.stream()
                .filter(TableValidator::tableValid)
                .forEach(file -> scriptCommands.script(file, false));
    }


}
