package com.mshmidov.roller.shell.command.supplier;

import com.mshmidov.roller.core.function.Functions;
import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.command.Verbose;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DiceCommand extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(DiceCommand.class);

    @CliCommand(value = "dice", help = "produces random number based on dice notation")
    public List<Integer> execute(
            @CliOption(key = "", help = "Conventional AD&D dice notation (eg '1d6+5' or '3d10')") final DiceExpression dice,
            @CliOption(key = { "times", "t" }, help = "repeat command more than one time", unspecifiedDefaultValue = "1") Integer times,
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        return IntStream.range(0, times)
                .mapToObj(i -> Functions.rollDice(dice))
                .collect(Collectors.toList());
    }
}
