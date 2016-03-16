package com.mshmidov.roller.shell.command.supplier;

import static com.mshmidov.roller.core.function.Functions.splitDefinition;

import com.mshmidov.roller.core.error.IncorrectTableDefinitionException;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableLoader;
import com.mshmidov.roller.core.service.TableRegistry;
import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.command.Expand;
import com.mshmidov.roller.shell.command.Verbose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefineTableCommand extends AbstractCommand {

    @Autowired private TableLoader tableLoader;

    @Autowired private TableRegistry tableRegistry;

    @CliCommand(value = "define table", help = "defines new table")
    public Table execute(
            @Expand @CliOption(key = "", help = "impromptu table definition") final String definition,
            @Expand @CliOption(key = "name", help = "impromptu table definition", mandatory = true) final String name,
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        final List<String> definitionLines = splitDefinition(definition);

        final Table table = tableLoader.createTable(name, definitionLines)
                .orElseThrow(() -> new IncorrectTableDefinitionException("Incorrect table definition: " + definition));

        tableRegistry.putTable(table);

        return table;
    }
}
