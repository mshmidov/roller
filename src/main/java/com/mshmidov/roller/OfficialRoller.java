package com.mshmidov.roller;

import com.mshmidov.roller.function.TableRegistry;

public class OfficialRoller {

    private final TableRegistry tableRegistry = TableRegistry.newInstance();

    public void roll() {

        tableRegistry.getAllTables().forEach(System.out::println);

    }

}
