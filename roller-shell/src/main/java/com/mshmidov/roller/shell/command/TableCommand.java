package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.core.function.Functions.disableDebugOutput;
import static com.mshmidov.roller.core.function.Replacement.replaceSubcommands;

import com.mshmidov.roller.core.function.Functions;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TableCommand implements CommandMarker {

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliAvailabilityIndicator(value = "table")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "table", help = "evaluates randomly chosen table row")
    public List<String> execute(
            @CliOption(key = "", help = "Table name") final Table table,
            @CliOption(key = { "dice", "d" }, help = "dice expression to use instead of table default") DiceExpression dice,
            @CliOption(key = { "times", "t" }, help = "repeat command more than one time", unspecifiedDefaultValue = "1") Integer times,
            @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        if (verbose) {
            Functions.enableDebugOutput("com.mshmidov.roller");
            Functions.enableDebugOutput("org.springframework.shell");
        }

        try {

            final IntSupplier diceToRoll = Optional.ofNullable(dice)
                    .map(Functions::diceRollSupplier)
                    .orElse(table.getRoll());

            return IntStream.range(0, times)
                    .mapToObj(i -> table.getValue(diceToRoll.getAsInt()))
                    .map(row -> replaceSubcommands(row, subcommand -> shell.executeNonInteractiveCommand(subcommand).getResult()))
                    .collect(Collectors.toList());

        } finally {
            if (verbose) {
                disableDebugOutput("com.mshmidov.roller");
                disableDebugOutput("org.springframework.shell");
            }
        }
    }


}
