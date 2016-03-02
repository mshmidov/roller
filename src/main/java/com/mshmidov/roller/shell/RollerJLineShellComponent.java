package com.mshmidov.roller.shell;

import jline.console.ConsoleReader;
import org.springframework.shell.core.JLineShellComponent;

import java.io.IOException;

public class RollerJLineShellComponent extends JLineShellComponent {

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
}
