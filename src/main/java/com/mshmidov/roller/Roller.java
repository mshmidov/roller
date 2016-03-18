package com.mshmidov.roller;

import com.mshmidov.roller.function.TableRegistry;
import com.mshmidov.roller.rollers.omorye.official.OfficialRoller;

import java.util.Objects;

public class Roller {

    public static void main(String[] args) {

        int times = (args.length > 1) ? Integer.parseInt(args[1]) : 1;

        if (args.length > 0) {

            if (Objects.equals(args[0], "official")) {

                final OfficialRoller roller = new OfficialRoller();
                for (int i = 0; i < times; i++) {
                    roller.roll();
                }
            }

        } else {
            TableRegistry.newInstance().getAllTables().forEach(table -> System.out.println(table.getName()));
        }
    }

}
