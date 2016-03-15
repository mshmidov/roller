package com.mshmidov.roller.shell;

import static com.google.common.base.MoreObjects.firstNonNull;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.TerminalSizeAware;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

public class RollerJLineShellComponent extends JLineShellComponent {

    private static final Logger logger = LoggerFactory.getLogger(RollerJLineShellComponent.class);

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
            logger.error(e.getMessage());
        }
    }

    public CommandResult executeNonInteractiveCommand(String line) {
        pushInteractive(false);
        try {
            logger.debug("Executing non-interactive command: "+line);
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
            System.out.println(((TerminalSizeAware) result).render(width).toString());

        } else if (isInteractive()) {
            System.out.println(result.toString());
        }
    }
}
