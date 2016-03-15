package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.core.function.Functions.disableDebugOutput;
import static com.mshmidov.roller.core.function.Functions.enableDebugOutput;
import static com.mshmidov.roller.core.function.Functions.splitDefinition;
import static com.mshmidov.roller.core.function.Functions.subcommandPattern;

import com.mshmidov.roller.core.error.IncorrectTableDefinitionException;
import com.mshmidov.roller.core.function.Functions;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableLoader;
import com.mshmidov.roller.shell.RollerJLineShellComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ChoiceCommand implements CommandMarker {

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @Autowired private TableLoader tableLoader;

    @CliAvailabilityIndicator(value = "choice")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "choice", help = "evaluates randomly chosen table row from impromptu table")
    public String execute(
            @CliOption(key = "", help = "impromptu table definition") final String definition,
            @CliOption(key = { "times", "t" }, help = "repeat command more than one time", unspecifiedDefaultValue = "1") Integer times,
            @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        if (verbose) {
            enableDebugOutput("com.mshmidov.roller");
            enableDebugOutput("org.springframework.shell");
        }

        try {
            final List<String> definitionLines = splitDefinition(definition);

            final Table choice = tableLoader.createTable("choice", definitionLines)
                    .orElseThrow(() -> new IncorrectTableDefinitionException("Incorrect table definition: " + definition));

            return IntStream.range(0, times)
                    .map(i -> choice.getRoll().getAsInt())
                    .mapToObj(choice::getValue)
                    .map(row -> Functions.replaceRegex(row, subcommandPattern(), 1,
                            match -> String.valueOf(shell.executeNonInteractiveCommand(match).getResult())))
                    .collect(Collectors.joining("\n"));

        } finally {
            if (verbose) {
                disableDebugOutput("com.mshmidov.roller");
                disableDebugOutput("org.springframework.shell");
            }
        }
    }
}
