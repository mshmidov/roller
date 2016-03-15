package com.mshmidov.roller.cli.command;

import static com.mshmidov.roller.core.function.Functions.splitDefinition;
import static com.mshmidov.roller.core.function.Replacement.replaceSubcommands;
import static com.mshmidov.roller.core.function.Replacement.subcommandPattern;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.mshmidov.roller.cli.Context;
import com.mshmidov.roller.cli.command.etc.OptionalIntegerConverter;
import com.mshmidov.roller.cli.command.etc.TimesValidator;
import com.mshmidov.roller.core.error.IncorrectDiceExpressionException;
import com.mshmidov.roller.core.error.IncorrectTableDefinitionException;
import com.mshmidov.roller.core.function.Replacement;
import com.mshmidov.roller.core.model.Table;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Parameters(commandNames = "choice")
public class ChoiceCommand implements Command {

    @Parameter(required = true, description = "impromptu table definition")
    private List<String> params;

    @Parameter(names = { "--times", "-t" }, description = "repeat command more than one time",
               converter = OptionalIntegerConverter.class, validateValueWith = TimesValidator.class)
    private Optional<Integer> times = Optional.empty();

    @Parameter(names = { "--verbose", "-v" }, description = "enable debug output")
    private boolean verbose;

    @Override
    public String execute(Context context) {

        try {

            final List<String> definitionLines = splitDefinition(params.get(0));

            final Table choice = context.tableLoader.createTable("choice", definitionLines)
                    .orElseThrow(() -> new IncorrectTableDefinitionException("Incorrect table definition: " + params.get(0)));

            return IntStream.range(0, times.orElse(1))
                    .map(i -> choice.getRoll().getAsInt())
                    .mapToObj(choice::getValue)
                    .map(row -> replaceSubcommands(row, subcommand -> context.newCommandLine().parse(subcommand.split(" ")).execute(context)))
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
