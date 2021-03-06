package com.mshmidov.roller.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.mshmidov.roller.cli.command.Command;
import com.mshmidov.roller.core.error.IncorrectUsageException;
import com.mshmidov.roller.core.error.InternalErrorException;
import com.mshmidov.roller.core.function.Functions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CommandLine {

    private final JCommander jCommander;

    private final Map<String, Command> commands = new HashMap<>();

    public CommandLine(JCommander jCommander, Collection<Command> commands) {

        this.jCommander = jCommander;

        for (Command command : commands) {

            final Parameters parameters = command.getClass().getAnnotation(Parameters.class);

            if (parameters == null) {
                throw new IllegalArgumentException("Invalid command class passed");
            }

            for (String name : parameters.commandNames()) {
                this.commands.put(name, command);
            }

            jCommander.addCommand(command);
        }
    }

    public Command parse(String... args) {
        try {
            jCommander.parse(args);
            final Command command = commands.get(jCommander.getParsedCommand());

            if (command == null) {
                throw new InternalErrorException("Cannot parse command");
            }

            if (command.isVerbose()) {
                Functions.enableDebugOutput("com.mshmidov.roller");
            }

            return command;

        } catch (ParameterException e) {

            final StringBuilder message = new StringBuilder();

            if (!isBlank(jCommander.getParsedCommand())) {
                jCommander.usage(jCommander.getParsedCommand(), message);
            } else {
                jCommander.usage(message);
            }

            throw new IncorrectUsageException(e, message.toString());
        }
    }
}
