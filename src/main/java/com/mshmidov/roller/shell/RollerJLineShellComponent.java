package com.mshmidov.roller.shell;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.springframework.shell.TerminalSizeAware;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;

import java.io.IOException;

public class RollerJLineShellComponent extends JLineShellComponent {

    private boolean interactive = true;

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public ConsoleReader getConsoleReader() {
        return reader;
    }

    public void println(CharSequence s) {
        try {
            reader.println(s);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public CommandResult executeNonInteractiveCommand(String line) {
        interactive = false;
        try {
            return super.executeCommand(line);
        } finally {
            interactive = true;
        }
    }

    @Override
    protected void handleExecutionResult(Object result) {
        if (result instanceof Iterable<?>) {
            for (Object o : (Iterable<?>) result) {
                handleExecutionResult(o);
            }
        } else if (result instanceof TerminalSizeAware && interactive) {
            int width = TerminalFactory.get().getWidth();
            logger.info(((TerminalSizeAware) result).render(width).toString());
        } else if (interactive) {
            logger.info(result.toString());
        }
    }
}
