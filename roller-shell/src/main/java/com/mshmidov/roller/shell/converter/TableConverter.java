package com.mshmidov.roller.shell.converter;

import static com.mshmidov.roller.core.function.Replacement.replaceSubcommands;

import com.mshmidov.roller.core.error.UnknownTableException;
import com.mshmidov.roller.core.model.Table;
import com.mshmidov.roller.core.service.TableRegistry;
import com.mshmidov.roller.shell.RollerJLineShellComponent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.Completion;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.MethodTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableConverter implements Converter<Table> {

    private static final Logger logger = LoggerFactory.getLogger(TableConverter.class);

    @Autowired(required = false) protected RollerJLineShellComponent shell;

    @Autowired private TableRegistry tableRegistry;



    @Override
    public Table convertFromText(final String value, final Class<?> requiredType, final String optionContext) {

        final String expandedValue = replaceSubcommands(value, subcommand -> shell.executeNonInteractiveCommand(subcommand).getResult());

        return tableRegistry.getTable(expandedValue)
                .orElseThrow(() -> new UnknownTableException("Unknown table: " + expandedValue));
    }

    @Override
    public boolean getAllPossibleValues(final List<Completion> completions, final Class<?> requiredType,
            final String existingData, final String optionContext, final MethodTarget target) {

        tableRegistry.getAllTables().stream()
                .filter(table -> StringUtils.isBlank(existingData) || table.getName().startsWith(existingData))
                .map(table -> new Completion(table.getName()))
                .forEach(completions::add);

        return true;
    }

    @Override
    public boolean supports(final Class<?> requiredType, final String optionContext) {
        return Table.class.isAssignableFrom(requiredType);
    }
}
