package com.mshmidov.roller.shell.command;

import com.google.common.collect.Iterables;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.notation.DiceExpressionComponent;
import com.wandrell.tabletop.dice.notation.calculator.DefaultDiceNotationCalculator;
import com.wandrell.tabletop.dice.notation.operation.Operand;
import com.wandrell.tabletop.dice.notation.operation.Operation;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;
import com.wandrell.tabletop.dice.roller.Roller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class RollCommand implements CommandMarker {

    @CliAvailabilityIndicator(value = "roll")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "roll", help = "produces random number based on dice notation")
    public int execute(@CliOption(key = "", mandatory = true, help = "Conventional AD&D dice notation (eg '1d6+5' or '3d10')") final DiceExpression dice) {

        final DiceExpressionComponent component = Iterables.getOnlyElement(dice.getComponents());
        if (component instanceof Operation) {
            return ((Operation) component).operate().getValue();
        }
        if (component instanceof Operand) {
            return ((Operand) component).getValue();
        }

        throw new IllegalStateException();
    }
}
