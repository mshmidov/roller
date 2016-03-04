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

    public TreeMap<Integer, String> getRows() {
        return new TreeMap<>(rows);
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean checkContinuity(TreeMap<Integer, String> rows) {
        return rows.lastKey() - rows.firstKey() == rows.size() - 1;
    }
}
