package com.mshmidov.roller;

import static com.google.common.base.MoreObjects.firstNonNull;

import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.mshmidov.roller.shell.command.table.DiscoverTablesCommand;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.CommandLine;
import org.springframework.shell.core.ExitShellRequest;

@Configuration
@ComponentScan
public class RollerApplication {

    @Autowired(required = false) private CommandLine commandLine;
    @Autowired(required = false) private RollerJLineShellComponent shell;

    @Autowired private DiscoverTablesCommand discoverTablesCommand;


    @Bean
    public DiceExpressionParser diceExpressionParser() {
        return new DiceExpressionParser();
    }

    public ExitShellRequest run() {

        String[] commandsToExecuteAndThenQuit = commandLine.getShellCommandsToExecute();
        ExitShellRequest exitShellRequest = ExitShellRequest.NORMAL_EXIT;

        if (null != commandsToExecuteAndThenQuit) {

            for (String cmd : commandsToExecuteAndThenQuit) {
                if (!shell.executeCommand(cmd).isSuccess()) {
                    exitShellRequest = ExitShellRequest.FATAL_EXIT;
                    break;
                }
            }

        } else {

            discoverTablesCommand.execute();

            shell.start();
            shell.promptLoop();

            exitShellRequest = firstNonNull(shell.getExitShellRequest(), ExitShellRequest.NORMAL_EXIT);

            shell.waitForComplete();
        }

        return exitShellRequest;
    }
}
