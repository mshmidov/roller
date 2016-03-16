package com.mshmidov.roller.shell.command.supplier;

import com.mshmidov.roller.core.service.TableLoader;
import com.mshmidov.roller.core.service.TableRegistry;
import com.mshmidov.roller.shell.command.AbstractCommand;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;
import java.util.stream.Stream;

@Component
public class TableDiscoverCommand extends AbstractCommand {

    public static final String KEYWORD = "discover tables";

    @Autowired private TableRegistry tableRegistry;

    @Autowired private TableLoader tableLoader;

    @CliCommand(value = KEYWORD, help = "tries to recursively find and run all scripts named `*.table` starting from current directory")
    public void execute() {
        final Collection<File> tables = FileUtils.listFiles(new File("."), new String[] { "table" }, true);

        tables.stream()
                .map(tableLoader::loadTable)
                .flatMap(table -> table.map(Stream::of).orElse(Stream.empty()))
                .forEach(tableRegistry::putTable);
    }


}
