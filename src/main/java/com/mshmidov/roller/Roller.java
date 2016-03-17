package com.mshmidov.roller;

import com.mshmidov.roller.function.TableRegistry;

import java.util.Objects;

public class Roller {

    public static void main(String[] args) {

        if (args.length > 0) {

            if (Objects.equals(args[0], "official")) {
                new OfficialRoller().roll();
            }

        } else {
            TableRegistry.newInstance().getAllTables().forEach(table -> System.out.println(table.getName()));
        }
    }

}
