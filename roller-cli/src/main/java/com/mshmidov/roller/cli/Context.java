package com.mshmidov.roller.cli;

import com.beust.jcommander.JCommander;
import com.mshmidov.roller.cli.command.Command;
import com.mshmidov.roller.cli.command.CommandLine;
import com.mshmidov.roller.core.service.TableLoader;
import com.mshmidov.roller.core.service.TableRegistry;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;

public final class Context {

    public final JCommander jCommander;
    public final CommandLine commandLine;
    public final DiceExpressionParser diceExpressionParser;
    public final TableLoader tableLoader;
    public final TableRegistry tableRegistry;

    public Context(Command... commands) {
        this.jCommander = new JCommander();
        this.commandLine = new CommandLine(jCommander, commands);
        this.diceExpressionParser = new DiceExpressionParser();
        this.tableLoader = new TableLoader(diceExpressionParser);
        this.tableRegistry = new TableRegistry();
    }
}
