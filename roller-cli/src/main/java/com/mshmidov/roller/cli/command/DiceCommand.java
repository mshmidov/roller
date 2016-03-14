package com.mshmidov.roller.cli.command;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.mshmidov.roller.cli.Context;
import com.mshmidov.roller.core.function.Functions;
import com.wandrell.tabletop.dice.notation.DiceExpression;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Parameters(commandNames = "dice")
public class DiceCommand implements Command {

    @Parameter(required = true, description = "<dice expression>")
    private List<String> params;

    @Parameter(names = { "--times", "-t" }, description = "repeat command more than one time", validateValueWith = TimesValidator.class)
    private Integer times;

    @Parameter(names = { "--verbose", "-v" }, description = "enable debug output")
    private boolean verbose;

    @Override
    public void execute(Context context) {

        try {
            final DiceExpression diceExpression = context.diceExpressionParser.parse(params.get(0));

            IntStream.range(0, Optional.ofNullable(times).orElse(1))
                    .map(i -> Functions.rollDice(diceExpression))
                    .forEach(System.out::println);

        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }

    @Override
    public boolean isVerbose() {
        return verbose;
    }


    public static class TimesValidator implements IValueValidator<Integer> {

        @Override
        public void validate(String name, Integer value) throws ParameterException {
            if (value <= 0) {
                throw new ParameterException(String.format("Should be positive number (\"%s\")", name));
            }
        }
    }
}
