package com.mshmidov.roller.cli.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.util.HashMap;
import java.util.Map;

public final class CommandLine {

    private final JCommander jCommander;

    private final Map<String, Command> commands = new HashMap<>();

    public CommandLine(JCommander jCommander, Command... commands) {

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
        jCommander.parse(args);
        final Command command = commands.get(jCommander.getParsedCommand());

        if (command == null) {
            throw new ParameterException("Illegal command");
        }

        return command;
    }
}
