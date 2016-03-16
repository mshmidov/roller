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
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        return variables.get(name).orElseThrow(() -> new UnknownVariableException("Unknown variable: " + name));
    }


}
