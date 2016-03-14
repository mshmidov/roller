package com.mshmidov.roller.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.ParameterException;
import com.mshmidov.roller.cli.command.Command;
import com.mshmidov.roller.cli.command.CommandLine;
import com.mshmidov.roller.cli.command.DiceCommand;
import com.mshmidov.roller.cli.command.HelpCommand;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableLoader;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Stream;

public class RollerCli {

    public static void main(String[] args) {

        final Context context = new Context();
        context.jCommander.setProgramName("roll.jar");

        final HelpCommand helpCommand = new HelpCommand();
        final DiceCommand diceCommand = new DiceCommand();

        final CommandLine commandLine = new CommandLine(context.jCommander, helpCommand, diceCommand);
        try {
            final Command command = commandLine.parse(args);

            if (command.isVerbose()) {
                final Logger root = (Logger) LoggerFactory.getLogger("com.mshmidov.roller");
                root.setLevel(Level.DEBUG);
            }

            discoverTables(new File("."), context.tableLoader).forEach(context.tableRegistry::putTable);

            command.execute(context);

        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            if (!isBlank(context.jCommander.getParsedCommand())) {
                context.jCommander.usage(context.jCommander.getParsedCommand());
            } else {
                context.jCommander.usage();
            }

            System.exit(1);
        }
    }

    private static Stream<Table> discoverTables(File directory, TableLoader tableLoader) {
        return FileUtils.listFiles(directory, new String[] { "table" }, true).stream()
                .map(tableLoader::loadTable)
                .flatMap(table -> table.map(Stream::of).orElse(Stream.empty()));
    }
}
