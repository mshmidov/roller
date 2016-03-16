package com.mshmidov.roller.shell.command.variable;

import com.google.common.base.Joiner;
import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.variables.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

@Component
public class VariableListCommand extends AbstractCommand {

    private static final Joiner.MapJoiner MAP_JOINER = Joiner.on(System.lineSeparator()).withKeyValueSeparator("=");

    @Autowired private Variables variables;

    @CliCommand(value = "list variables", help = "lists all known variables")
    public String execute() {
        return MAP_JOINER.join(variables.getAll());
    }
}
