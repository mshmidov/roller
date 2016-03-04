package com.mshmidov.roller.function;

import com.mshmidov.roller.model.Table;

import java.util.function.Function;

public final class RenderTable implements Function<Table, String> {

    @Override
    public String apply(Table table) {

        final StringBuilder builder = new StringBuilder(table.getName()).append('\n');
        table.getRows().forEach((i, s) -> builder.append(i).append(": ").append(s).append('\n'));

        return builder.toString();
    }

    public static RenderTable renderTable() {
        return new RenderTable();
    }
}
