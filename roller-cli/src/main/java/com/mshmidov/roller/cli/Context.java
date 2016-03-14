package com.mshmidov.roller.cli;

import com.beust.jcommander.JCommander;
import com.mshmidov.roller.cli.command.Command;
import com.mshmidov.roller.core.service.TableLoader;
import com.mshmidov.roller.core.service.TableRegistry;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;

import java.util.List;
import java.util.function.Supplier;

public final class Context {

    private final Supplier<List<Command>> commands;

    public final JCommander jCommander;
    public final DiceExpressionParser diceExpressionParser;
    public final TableLoader tableLoader;
    public final TableRegistry tableRegistry;

    public Context(Supplier<List<Command>> commands) {
        this.commands = commands;

        this.jCommander = new JCommander();
        this.diceExpressionParser = new DiceExpressionParser();
        this.tableLoader = new TableLoader(diceExpressionParser);
        this.tableRegistry = new TableRegistry();
    }

    public CommandLine newCommandLine() {
        return new CommandLine(jCommander, commands.get());
    }
}
