package com.mshmidov.roller.shell.command.table;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.context.table.TableBuildingContext;
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

@Component
public class TableDiceCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(TableDiceCommand.class);

    @Autowired CurrentContext context;

    @CliAvailabilityIndicator(value = "dice")
    public boolean isAvailable() {
        return context.getInteractiveContext().map(c -> c instanceof TableBuildingContext).orElse(false);
    }

    @CliCommand(value = "dice", help = "defines default dice roll for table that is currently being created")
    public void execute(@CliOption(key = { "" }, mandatory = true, help = "Conventional AD&D dice notation (eg '1d6+5' or '3d10')") final DiceExpression dice) {
        context.getInteractiveContext()
                .flatMap(c -> (c instanceof TableBuildingContext) ? Optional.of((TableBuildingContext) c) : Optional.empty())
                .ifPresent(builder -> builder.setRoll(dice));

    }
}
