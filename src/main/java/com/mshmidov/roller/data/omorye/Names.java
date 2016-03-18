package com.mshmidov.roller.data.omorye;

import static com.mshmidov.roller.rollers.Sex.MALE;
import static com.mshmidov.roller.rollers.omorye.Descent.BOYAR;
import static com.mshmidov.roller.rollers.omorye.Descent.NOBLE;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.roller.function.RandomChoice;
import com.mshmidov.roller.function.TableRegistry;
import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.rollers.Sex;
import com.mshmidov.roller.rollers.omorye.Descent;

import java.util.Map;

public final class Names {

    private final TableRegistry tableRegistry;

    private static final Map<Descent, String> SURNAME_TABLE_NAME = ImmutableMap.<Descent, String>builder()
            .put(Descent.PEASANT, "omorye-surname-common")
            .put(Descent.BURGHER, "omorye-surname-common")
            .put(Descent.MERCHANT, "omorye-surname-common")
            .put(NOBLE, "omorye-surname-noble")
            .put(BOYAR, "omorye-surname-boyar")
            .build();

    public Names(TableRegistry tableRegistry) {
        this.tableRegistry = tableRegistry;
    }


    public String randomName(Sex sex) {
        final String sexSuffix = (sex == MALE) ? "-male" : "-female";
        final String topSuffix = RandomChoice.byChance(0.7, "-top").orElse("");

        final Table<String> nameTable = tableRegistry.getTable("omorye-name" + sexSuffix + topSuffix).get();
        return nameTable.rollValue();
    }

    public String randomSurname(Descent descent, Sex sex) {

        final Table<String> surnameTable = tableRegistry.getTable(SURNAME_TABLE_NAME.get(descent)).get();

        final boolean doubleSurname = (descent == NOBLE || descent == BOYAR) && RandomChoice.byChance(0.2);

        final String surname = rollSurname(surnameTable, sex);

        if (doubleSurname) {
            final Table<String> secondSurnameTable = tableRegistry.getTable(SURNAME_TABLE_NAME.get(RandomChoice.from(NOBLE, BOYAR))).get();
            return String.format("%s-%s", surname, rollSurname(secondSurnameTable, sex));

        } else {
            return surname;
        }
    }

    public String femaleSurname(String maleForm) {

        if (maleForm.endsWith("ов") || maleForm.endsWith("ев") || maleForm.endsWith("ин")) {
            return maleForm + "а";
        }

        if (maleForm.endsWith("ий") || maleForm.endsWith("ый") || maleForm.endsWith("ой")) {
            return maleForm.substring(0, maleForm.length() - 2) + "ая";
        }

        return maleForm;
    }

    private String rollSurname(Table<String> table, Sex sex) {
        final String surname = table.rollValue();
        return (sex == MALE) ? surname : femaleSurname(surname);
    }
}
