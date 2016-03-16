package com.mshmidov.roller.shell.command.variable;

import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.command.Expand;
import com.mshmidov.roller.shell.command.Verbose;
import com.mshmidov.roller.shell.variables.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class VariableSetCommand extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(VariableSetCommand.class);

    @Autowired private Variables variables;

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliCommand(value = "set", help = "set a value to variable")
    public String execute(
            @Expand @CliOption(key = "", help = "variable name") final String name,
            @Expand @CliOption(key = "value", help = "value") final String value,
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {


        if (value != null) {
            variables.set(name, value);
            return value;

        } else {
            variables.remove(name);
            return null;
        }
    }
}
