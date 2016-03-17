package com.mshmidov.roller.rollers.official;

import static com.mshmidov.roller.rollers.official.Descent.BOYAR;
import static com.mshmidov.roller.rollers.official.Descent.NOBLE;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.roller.function.RandomChoice;
import com.mshmidov.roller.function.TableRegistry;
import com.mshmidov.roller.model.Table;

import java.util.Map;
import java.util.function.Supplier;

public class OfficialRoller {

    private final TableRegistry tableRegistry = TableRegistry.newInstance();

    private final Map<Descent, String> surnameTable = ImmutableMap.<Descent, String>builder()
            .put(Descent.PEASANT, "omorye-surname-common")
            .put(Descent.BURGHER, "omorye-surname-common")
            .put(Descent.MERCHANT, "omorye-surname-common")
            .put(NOBLE, "omorye-surname-noble")
            .put(BOYAR, "omorye-surname-boyar")
            .build();

    public void roll(int times) {
        for (int i = 0; i < times; i++) {
            generateCareer();
        }
    }

    private void generateCareer() {
        final Descent descent = RandomChoice.from(Descent.class);
        final Career career = RandomChoice.from(Career.class);

        final Table nameTable = tableRegistry.getTable(RandomChoice.byChance(0.7, "omorye-name-male-top").orElse("omorye-name-male")).get();

        final String name = nameTable.rollValue() + " " + getSurnameSupplier(descent).get();

        System.out.println(name);
    }

    private Supplier<String> getSurnameSupplier(Descent descent) {
        final Table surnameTable = tableRegistry.getTable(this.surnameTable.get(descent)).get();

        final boolean doubleSurname = (descent == NOBLE || descent == BOYAR) && RandomChoice.byChance(0.2);

        if (doubleSurname) {
            final Table secondSurnameTable = tableRegistry.getTable(this.surnameTable.get(RandomChoice.from(NOBLE, BOYAR))).get();
            return () -> String.format("%s-%s", surnameTable.rollValue(), secondSurnameTable.rollValue());

        } else {
            return surnameTable::rollValue;
        }
    }

}
