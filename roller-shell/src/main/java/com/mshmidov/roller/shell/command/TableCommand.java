package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.core.function.Functions.disableDebugOutput;

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

import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;

@Component
public class TableCommand implements CommandMarker {

    private static final Pattern SUBCOMMAND = Pattern.compile("\\{(.+)\\}");

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliAvailabilityIndicator(value = "table")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "table", help = "evaluates randomly chosen table row")
    public String execute(
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

            final String row = table.getValue(diceToRoll.getAsInt());

            return Functions.replaceRegex(row, SUBCOMMAND, 1,
                    match -> String.valueOf(shell.executeNonInteractiveCommand(match).getResult()));

        } finally {
            if (verbose) {
                disableDebugOutput("com.mshmidov.roller");
                disableDebugOutput("org.springframework.shell");
            }
        }
    }

}
