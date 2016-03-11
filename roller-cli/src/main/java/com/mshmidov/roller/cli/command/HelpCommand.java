package com.mshmidov.roller.cli.command;

import com.beust.jcommander.Parameters;
import com.mshmidov.roller.cli.Context;

@Parameters(commandNames = {"help", "--help", "-?"})
public class HelpCommand implements Command {

    @Override
    public void execute(Context context) {
        context.jCommander.usage();
    }

    @Override
    public boolean isVerbose() {
        return false;
    }
}
