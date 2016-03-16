package com.mshmidov.roller.shell.command.supplier;

import com.mshmidov.roller.core.function.Functions;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.command.Verbose;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TableCommand extends AbstractCommand {

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliCommand(value = "table", help = "evaluates randomly chosen table row")
    public List<String> execute(
            @CliOption(key = "", help = "Table name") final Table table,
            @CliOption(key = { "dice", "d" }, help = "dice expression to use instead of table default") DiceExpression dice,
            @CliOption(key = { "times", "t" }, help = "repeat command more than one time", unspecifiedDefaultValue = "1") Integer times,
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        final IntSupplier diceToRoll = Optional.ofNullable(dice)
                .map(Functions::diceRollSupplier)
                .orElse(table.getRoll());

        return IntStream.range(0, times)
                .mapToObj(i -> table.getValue(diceToRoll.getAsInt()))
                .map(this::expandArgument)
                .collect(Collectors.toList());

    }


}
