package com.mshmidov.roller.model;

import com.google.common.base.Preconditions;

import java.util.TreeMap;

public final class Table {

    private final String name;

    private final TreeMap<Integer, String> rows;

    public Table(String name, TreeMap<Integer, String> rows) {
        Preconditions.checkArgument(checkContinuity(rows), "Rows should be continuously numbered");
        this.name = name;
        this.rows = new TreeMap<>(rows);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(name).append('\n');

        rows.forEach((i, s) -> builder.append(i).append(": ").append(s).append('\n'));

        return builder.toString();
    }

    public static boolean checkContinuity(TreeMap<Integer, String> rows) {
        return rows.lastKey() - rows.firstKey() == rows.size() - 1;
    }
}
