package com.mshmidov.roller.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.mshmidov.roller.cli.Context;
import com.mshmidov.roller.cli.command.etc.OptionalIntegerConverter;
import com.mshmidov.roller.cli.command.etc.TimesValidator;
import com.mshmidov.roller.core.error.IncorrectDiceExpressionException;
import com.mshmidov.roller.core.function.Functions;
import com.wandrell.tabletop.dice.notation.DiceExpression;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Parameters(commandNames = "dice")
public class DiceCommand implements Command {

    @Parameter(required = true, description = "<dice expression>")
    private List<String> params;

    @Parameter(names = { "--times", "-t" }, description = "repeat command more than one time",
               converter = OptionalIntegerConverter.class, validateValueWith = TimesValidator.class)
    private Optional<Integer> times = Optional.empty();

    @Parameter(names = { "--verbose", "-v" }, description = "enable debug output")
    private boolean verbose;

    @Override
    public String execute(Context context) {

        try {
            final DiceExpression diceExpression = context.diceExpressionParser.parse(params.get(0));

            return IntStream.range(0, times.orElse(1))
                    .map(i -> Functions.rollDice(diceExpression))
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining("\n"));

        } catch (IllegalStateException e) {
            throw new IncorrectDiceExpressionException(e);
        }
    }

    @Override
    public boolean isVerbose() {
        return verbose;
    }
}
