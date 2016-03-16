package com.mshmidov.roller.shell.command.supplier;

import static com.mshmidov.roller.core.function.Functions.splitDefinition;

import com.mshmidov.roller.core.error.IncorrectTableDefinitionException;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableLoader;
import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.command.Expand;
import com.mshmidov.roller.shell.command.Verbose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ChoiceCommand extends AbstractCommand {

    @Autowired private TableLoader tableLoader;

    @CliCommand(value = "choice", help = "evaluates randomly chosen table row from impromptu table")
    public List<String> execute(
            @CliOption(key = "", help = "impromptu table definition") @Expand final String definition,
            @CliOption(key = { "times", "t" }, help = "repeat command more than one time", unspecifiedDefaultValue = "1") Integer times,
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        final List<String> definitionLines = splitDefinition(definition);

        final Table choice = tableLoader.createTable("choice", definitionLines)
                .orElseThrow(() -> new IncorrectTableDefinitionException("Incorrect table definition: " + definition));

        final IntSupplier roll = choice.getRoll();

        return IntStream.range(0, times)
                .map(i -> roll.getAsInt())
                .mapToObj(choice::getValue)
                .map(this::expandArgument)
                .collect(Collectors.toList());

    }
}
