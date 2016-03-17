package com.mshmidov.roller.rollers.omorye.official;


import static com.mshmidov.roller.rollers.Sex.MALE;
import static com.mshmidov.roller.rollers.omorye.Descent.BOYAR;
import static com.mshmidov.roller.rollers.omorye.Descent.BURGHER;
import static com.mshmidov.roller.rollers.omorye.Descent.MERCHANT;
import static com.mshmidov.roller.rollers.omorye.Descent.NOBLE;
import static com.mshmidov.roller.rollers.omorye.Descent.PEASANT;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.roller.function.RandomChoice;
import com.mshmidov.roller.function.TableBuilder;
import com.mshmidov.roller.function.TableRegistry;
import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.rollers.omorye.Descent;
import com.mshmidov.roller.rollers.omorye.OmoryanNames;

import java.util.Map;
import java.util.Optional;

public class OfficialRoller {

    private final TableRegistry tableRegistry = TableRegistry.newInstance();
    private final OmoryanNames names = new OmoryanNames(tableRegistry);

    private final Table<Rank> rankTable = new TableBuilder<Rank>("rank")
            .row(new Range(1, 30), Rank.LOW)
            .row(new Range(31, 70), Rank.AVERAGE)
            .row(new Range(71, 90), Rank.HIGH)
            .row(new Range(91, 100), Rank.HIGHEST)
            .build(Optional.empty()).get();

    private final Map<Descent, Integer> descentRankModifier = ImmutableMap.of(
            PEASANT, -15,
            BURGHER, 0,
            MERCHANT, +5,
            NOBLE, +10,
            BOYAR, +20);

    public void roll() {

        final Descent descent = RandomChoice.from(Descent.class);
        final Career career = RandomChoice.from(Career.class);
        final Rank rank = this.rankTable.rollValue(descentRankModifier.get(descent));

        final String name = names.randomName(MALE) + " " + names.randomSurname(descent, MALE);

        final StringBuilder output = new StringBuilder();
        output.append(name).append(", ").append(descent).append(", ").append(career).append(", ").append(rank);
        System.out.println(output);
    }

}
