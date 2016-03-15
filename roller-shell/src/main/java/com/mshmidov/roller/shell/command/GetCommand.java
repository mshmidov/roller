package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.core.function.Functions.disableDebugOutput;
import static com.mshmidov.roller.core.function.Functions.enableDebugOutput;
import static com.mshmidov.roller.core.function.Replacement.replaceSubcommands;

import com.mshmidov.roller.core.error.IncorrectVariableNameException;
import com.mshmidov.roller.core.error.UnknownVariableException;
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

import java.util.Optional;

@Component
public class GetCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(GetCommand.class);

    @Autowired private Variables variables;

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliAvailabilityIndicator(value = "get")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "get", help = "get a variable value")
    public String execute(
            @CliOption(key = "", help = "variable name") final String name,
            @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        if (verbose) {
            enableDebugOutput("com.mshmidov.roller");
            enableDebugOutput("org.springframework.shell");
        }

        try {

            final String expandedName = replaceSubcommands(String.valueOf(name), subcommand -> shell.executeNonInteractiveCommand(subcommand).getResult());
            final Optional<String> value = variables.get(expandedName);

            if (value.isPresent()) {
                logger.debug("Variable {}={}", expandedName, value.get());
            } else {
                logger.debug("Variable {} is unknown", expandedName);
            }

            return value.orElseThrow(() -> new UnknownVariableException("Unknown variable: " + expandedName));

        } finally {
            if (verbose) {
                disableDebugOutput("com.mshmidov.roller");
                disableDebugOutput("org.springframework.shell");
            }
        }
    }


}
