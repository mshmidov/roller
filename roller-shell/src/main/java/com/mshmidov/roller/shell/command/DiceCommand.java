package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.core.function.Functions.disableDebugOutput;
import static com.mshmidov.roller.core.function.Functions.enableDebugOutput;

import com.mshmidov.roller.core.function.Functions;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DiceCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(DiceCommand.class);

    @CliAvailabilityIndicator(value = "dice")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "dice", help = "produces random number based on dice notation")
    public String execute(
            @CliOption(key = "", help = "Conventional AD&D dice notation (eg '1d6+5' or '3d10')") final DiceExpression dice,
            @CliOption(key = { "times", "t" }, help = "repeat command more than one time", unspecifiedDefaultValue = "1") Integer times,
            @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        if (verbose) {
            enableDebugOutput("com.mshmidov.roller");
            enableDebugOutput("org.springframework.shell");
        }

        try {

            return IntStream.range(0, times)
                    .map(i -> Functions.rollDice(dice))
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining("\n"));

        } finally {
            if (verbose) {
                disableDebugOutput("com.mshmidov.roller");
                disableDebugOutput("org.springframework.shell");
            }
        }
    }


}
