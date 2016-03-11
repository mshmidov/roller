package com.mshmidov.roller.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.mshmidov.roller.cli.Context;
import com.mshmidov.roller.core.function.Functions;
import com.wandrell.tabletop.dice.notation.DiceExpression;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Parameters(commandNames = "dice")
public class DiceCommand implements Command {

    @Parameter(required = true)
    private List<String> params;

    @Parameter(names = { "--times", "-t" })
    private Integer times;

    @Parameter(names = { "--verbose", "-v" })
    private boolean verbose;

    @Override
    public void execute(Context context) {

        try {
            final DiceExpression diceExpression = context.diceExpressionParser.parse(params.get(0));

            IntStream.range(0, Optional.ofNullable(times).orElse(1))
                    .map(i ->Functions.rollDice(diceExpression))
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
}
