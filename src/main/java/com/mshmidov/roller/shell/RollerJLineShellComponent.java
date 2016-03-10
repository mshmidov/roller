package com.mshmidov.roller.shell;

import static com.google.common.base.MoreObjects.firstNonNull;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.springframework.shell.TerminalSizeAware;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

public class RollerJLineShellComponent extends JLineShellComponent {

    private final Deque<Boolean> interactive = new LinkedList<>();

    public boolean isInteractive() {
        return firstNonNull(interactive.peek(), true);
    }

    public void pushInteractive(boolean interactive) {
        this.interactive.push(interactive);
    }

    public void popInteractive() {
        this.interactive.pop();
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
        pushInteractive(false);
        try {
            return super.executeCommand(line);
        } finally {
            popInteractive();
        }
    }

    @Override
    protected void handleExecutionResult(Object result) {
        if (result instanceof Iterable<?>) {
            for (Object o : (Iterable<?>) result) {
                handleExecutionResult(o);
            }

        } else if (result instanceof TerminalSizeAware && isInteractive()) {
            int width = TerminalFactory.get().getWidth();
            logger.info(((TerminalSizeAware) result).render(width).toString());

        } else if (isInteractive()) {
            logger.info(result.toString());
        }
    }
}
