package com.mshmidov.roller.shell.command;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;

import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.mshmidov.roller.core.function.Functions;
import com.mshmidov.roller.core.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RollCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(RollCommand.class);

    private static final Pattern SUBCOMMAND = Pattern.compile("\\{.+\\}");

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliAvailabilityIndicator(value = "roll")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "roll", help = "produces random number based on dice notation")
    public String execute(
            @CliOption(key = "", help = "Conventional AD&D dice notation (eg '1d6+5' or '3d10')") final DiceExpression dice,
            @CliOption(key = {"table"}, help = "Table to roll") Table table) {

        if (table == null && dice != null) {
            return String.valueOf(Functions.rollDice(dice));
        }

        if (table != null) {
            final IntSupplier diceToRoll = Optional.ofNullable(dice)
                    .map(Functions::diceRollSupplier)
                    .orElse(table.getRoll());

            final String row = table.getValue(diceToRoll.getAsInt());


            return Functions.replaceRegex(row, SUBCOMMAND,
                    s -> String.valueOf(shell.executeNonInteractiveCommand(removeEnd(removeStart(s, "{"), "}")).getResult()));
        }

        logger.error("At least one parameter for this command should be supplied");
        return null;
    }


}
