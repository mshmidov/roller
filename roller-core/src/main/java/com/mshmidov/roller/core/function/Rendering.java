package com.mshmidov.roller.core.function;

import com.google.common.base.Joiner;
import com.mshmidov.roller.core.model.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class Rendering {

    private Rendering() {}

    public static String renderTable(Table table) {

        final List<String> parts = new ArrayList<>();
        parts.add(table.getName());

        table.getRows().forEach((range, s) -> parts.add(String.format("%s: %s", range.toString(), s)));

        return Joiner.on('\n').join(parts);
    }

    public static String renderCommandResult(Object result) {
        if (result instanceof Iterable<?>) {
            return StreamSupport.stream(((Iterable<?>) result).spliterator(), false)
                    .map(Rendering::renderCommandResult)
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        return String.valueOf(result);
    }
}
