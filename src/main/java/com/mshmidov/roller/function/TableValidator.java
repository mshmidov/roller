package com.mshmidov.roller.function;

import com.mshmidov.roller.shell.command.DoneCommand;
import com.mshmidov.roller.shell.command.table.TableCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public final class TableValidator {

    private static final Logger logger = LoggerFactory.getLogger(TableValidator.class);

    private TableValidator() {
    }

    public static boolean tableValid(File file) {

        try {
            final LinkedList<String> lines = new LinkedList<>(Files.lines(file.toPath())
                    .filter(line -> !line.startsWith("//"))
                    .filter(line -> !line.startsWith(";"))
                    .collect(Collectors.toList()));

            return !lines.isEmpty()
                    && Objects.equals(lines.getFirst(), TableCommand.KEYWORD + " " + StringUtils.removeEnd(file.getName(), ".table"))
                    && Objects.equals(lines.getLast(), DoneCommand.KEYWORD)
                    && lines.stream().filter(line -> line.startsWith(TableCommand.KEYWORD + " ")).collect(Collectors.counting()) == 1
                    && lines.stream().filter(line -> line.equals(TableCommand.KEYWORD)).collect(Collectors.counting()) == 0
                    && lines.stream().filter(line -> line.equals(DoneCommand.KEYWORD)).collect(Collectors.counting()) == 1;

        } catch (IOException e) {
            logger.error("Cannot read file " + file.toString(), e);
            return false;
        }
    }
}
