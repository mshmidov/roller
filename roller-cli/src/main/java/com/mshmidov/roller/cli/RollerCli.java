package com.mshmidov.roller.cli;

import com.google.common.collect.ImmutableList;
import com.mshmidov.roller.cli.command.ChoiceCommand;
import com.mshmidov.roller.cli.command.Command;
import com.mshmidov.roller.cli.command.DiceCommand;
import com.mshmidov.roller.cli.command.TableCommand;
import com.mshmidov.roller.cli.error.AbnormalExitException;
import com.mshmidov.roller.cli.error.IncorrectUsageException;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.stream.Stream;

public class RollerCli {

    public static void main(String[] args) {

        final Context context = new Context(() -> ImmutableList.of(new DiceCommand(), new TableCommand(), new ChoiceCommand()));
        context.jCommander.setProgramName("roll.jar");

        discoverTables(new File("."), context.tableLoader).forEach(context.tableRegistry::putTable);

        try {
            final Command command = context.newCommandLine().parse(args);

            System.out.println(command.execute(context));

        } catch (IncorrectUsageException e) {
            System.err.println(e.getMessage());
            System.out.println(e.getUsage());
            System.exit(e.getCode());

        } catch (AbnormalExitException e) {
            System.err.println(e.getMessage());
            System.exit(e.getCode());
        }
    }

    private static Stream<Table> discoverTables(File directory, TableLoader tableLoader) {
        return FileUtils.listFiles(directory, new String[] { "table" }, true).stream()
                .map(tableLoader::loadTable)
                .flatMap(table -> table.map(Stream::of).orElse(Stream.empty()));
    }
}
