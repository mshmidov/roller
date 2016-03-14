package com.mshmidov.roller.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.mshmidov.roller.cli.command.Command;
import com.mshmidov.roller.cli.command.DiceCommand;
import com.mshmidov.roller.cli.error.AbnormalExitException;
import com.mshmidov.roller.cli.error.IncorrectUsageException;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableLoader;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Stream;

public class RollerCli {

    private final Context context;

    public static void main(String[] args) {

        final Context context = new Context(new DiceCommand());
        context.jCommander.setProgramName("roll.jar");

        final RollerCli rollerCli = new RollerCli(context);

        System.out.println(rollerCli.execute(args));
    }

    public RollerCli(Context context) {
        this.context = context;
        discoverTables(new File("."), context.tableLoader).forEach(context.tableRegistry::putTable);
    }

    private String execute(String[] args) {
        try {
            final Command command = context.commandLine.parse(args);

            if (command.isVerbose()) {
                final Logger root = (Logger) LoggerFactory.getLogger("com.mshmidov.roller");
                root.setLevel(Level.DEBUG);
            }

            return command.execute(context);

        } catch (IncorrectUsageException e) {
            System.err.println(e.getMessage());
            System.out.println(e.getUsage());
            System.exit(e.getCode());

        } catch (AbnormalExitException e) {
            System.err.println(e.getMessage());
            System.exit(e.getCode());
        }
        return null;
    }

    private Stream<Table> discoverTables(File directory, TableLoader tableLoader) {
        return FileUtils.listFiles(directory, new String[] { "table" }, true).stream()
                .map(tableLoader::loadTable)
                .flatMap(table -> table.map(Stream::of).orElse(Stream.empty()));
    }
}
