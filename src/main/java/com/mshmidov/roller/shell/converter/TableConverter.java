package com.mshmidov.roller.shell.converter;

import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.service.TableRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.Completion;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.MethodTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableConverter implements Converter<Table> {

    @Autowired private TableRegistry tableRegistry;

    @Override
    public Table convertFromText(final String value, final Class<?> requiredType, final String optionContext) {
        return tableRegistry.getTable(value).orElse(null);
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
