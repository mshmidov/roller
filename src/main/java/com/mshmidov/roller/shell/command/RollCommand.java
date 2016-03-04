package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.function.RollDice.rollDice;

import com.mshmidov.roller.function.RollDice;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Component
public class RollCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(RollCommand.class);

    @CliAvailabilityIndicator(value = "roll")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "roll", help = "produces random number based on dice notation")
    public String execute(
            @CliOption(key = "", help = "Conventional AD&D dice notation (eg '1d6+5' or '3d10')") final DiceExpression dice,
            @CliOption(key = "table", help = "Table to roll") Table table) {

        if (table == null && dice != null) {
            return String.valueOf(rollDice(dice).get());
        }

        if (table != null) {
            final Supplier<Integer> diceToRoll = Optional.ofNullable(dice)
                    .map(RollDice::rollDice)
                    .orElseGet(table::getRoll);

            return table.getValue(diceToRoll.get());
        }

        logger.error("At least one parameter for this command should be supplied");
        return null;
    }


}
