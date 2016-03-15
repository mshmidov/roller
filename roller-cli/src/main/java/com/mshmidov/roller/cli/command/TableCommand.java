package com.mshmidov.roller.cli.command;

import static com.mshmidov.roller.core.function.Replacement.replaceSubcommands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.mshmidov.roller.cli.Context;
import com.mshmidov.roller.cli.command.etc.OptionalIntegerConverter;
import com.mshmidov.roller.cli.command.etc.OptionalStringConverter;
import com.mshmidov.roller.cli.command.etc.TimesValidator;
import com.mshmidov.roller.core.error.IncorrectDiceExpressionException;
import com.mshmidov.roller.core.error.UnknownTableException;
import com.mshmidov.roller.core.function.Functions;
import com.mshmidov.roller.core.model.Table;

import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Parameters(commandNames = "table")
public class TableCommand implements Command {

    private static final Pattern SUBCOMMAND = Pattern.compile("\\{(.+)\\}");

    @Parameter(required = true, description = "<table name>")
    private List<String> params;

    @Parameter(names = { "--times", "-t" }, description = "repeat command more than one time",
               converter = OptionalIntegerConverter.class, validateValueWith = TimesValidator.class)
    private Optional<Integer> times = Optional.empty();

    @Parameter(names = { "--dice", "-d" }, description = "dice expression to use instead of table default", converter = OptionalStringConverter.class)
    private Optional<String> dice = Optional.empty();

    @Parameter(names = { "--verbose", "-v" }, description = "enable debug output")
    private boolean verbose;

    @Override
    public String execute(Context context) {

        try {
            final String tableName = params.get(0);

            final Table table = context.tableRegistry.getTable(tableName)
                    .orElseThrow(() -> new UnknownTableException("Unknown table: " + tableName));

            final IntSupplier roll = dice.map(context.diceExpressionParser::parse).map(Functions::diceRollSupplier)
                    .orElse(table.getRoll());

            return IntStream.range(0, times.orElse(1))
                    .map(i -> roll.getAsInt())
                    .mapToObj(table::getValue)
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
