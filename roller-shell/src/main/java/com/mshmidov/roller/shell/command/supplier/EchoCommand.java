package com.mshmidov.roller.shell.command.supplier;

import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.command.Expand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class EchoCommand extends AbstractCommand {

    @CliCommand(value = "echo", help = "expands and prints argument")
    public String execute(@Expand @CliOption(key = "", help = "string to print") String s) {
        return s;
    }
}
