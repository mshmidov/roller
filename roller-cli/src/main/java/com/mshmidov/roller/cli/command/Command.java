package com.mshmidov.roller.cli.command;

import com.mshmidov.roller.cli.Context;

public interface Command {

    String execute(Context context);

    boolean isVerbose();

}
