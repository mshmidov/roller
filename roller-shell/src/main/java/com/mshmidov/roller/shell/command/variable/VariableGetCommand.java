package com.mshmidov.roller.shell.command.variable;

import com.mshmidov.roller.core.error.UnknownVariableException;
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

import java.util.Optional;

@Component
public class VariableGetCommand extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(VariableGetCommand.class);

    @Autowired private Variables variables;

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliCommand(value = "get", help = "get a variable value")
    public String execute(
            @Expand @CliOption(key = "", help = "variable name") final String name,
            @CliOption(key = "defined", help = "return if variable is defined", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean checkDefined,
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        final Optional<String> value = variables.get(name);
        return checkDefined
                ? String.valueOf(value.isPresent())
                : value.orElseThrow(() -> new UnknownVariableException("Unknown variable: " + name));
    }


}
