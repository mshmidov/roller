package com.mshmidov.roller.shell.command.etc;

import com.google.common.base.Joiner;
import com.mshmidov.roller.shell.variables.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ListVariablesCommand implements CommandMarker {

    private static final Joiner.MapJoiner MAP_JOINER = Joiner.on(System.lineSeparator()).withKeyValueSeparator("=");

    @Autowired private Variables variables;

    @CliAvailabilityIndicator(value = "list variables")
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = "list variables", help = "lists all known variables")
    public String execute() {
        return MAP_JOINER.join(variables.getAll());
    }
}
