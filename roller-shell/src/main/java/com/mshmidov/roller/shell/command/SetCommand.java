package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.core.function.Functions.disableDebugOutput;
import static com.mshmidov.roller.core.function.Functions.enableDebugOutput;
import static com.mshmidov.roller.core.function.Replacement.replaceSubcommands;

import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.mshmidov.roller.shell.variables.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class SetCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(SetCommand.class);

    @Autowired private Variables variables;

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliAvailabilityIndicator(value = "set")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "set", help = "set a value to variable")
    public String execute(
            @CliOption(key = "", help = "variable name") final String name,
            @CliOption(key = "value", help = "value") final String value,
            @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        if (verbose) {
            enableDebugOutput("com.mshmidov.roller");
            enableDebugOutput("org.springframework.shell");
        }

        try {

            final String expandedName = replaceSubcommands(name, subcommand -> shell.executeNonInteractiveCommand(subcommand).getResult());

            if (value != null) {
                final String expandedValue = replaceSubcommands(value, subcommand -> shell.executeNonInteractiveCommand(subcommand).getResult());

                logger.debug("Setting variable {}={}", expandedName, expandedValue);
                variables.set(expandedName, expandedValue);

                return expandedValue;

            } else {
                logger.debug("Removing variable {}", expandedName);
                variables.remove(expandedName);
                return null;
            }


        } finally {
            if (verbose) {
                disableDebugOutput("com.mshmidov.roller");
                disableDebugOutput("org.springframework.shell");
            }
        }
    }
}
