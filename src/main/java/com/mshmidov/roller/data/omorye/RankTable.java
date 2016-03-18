package com.mshmidov.roller.data.omorye;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.mshmidov.roller.data.omorye.Career.MILITARY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.CAVALRY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.FLEET;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.GUARDS_CAVALRY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.GUARDS_INFANTRY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.INFANTRY;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.mshmidov.roller.function.RandomChoice;
import com.mshmidov.roller.model.Range;

import java.util.Map;
import java.util.NavigableMap;

public final class RankTable {

    public static final Range GRADES = new Range(1, 13);


    private static final Map<MilitaryBranch, NavigableMap<Integer, String>> MILITARY_RANKS = ImmutableMap.of(
            INFANTRY, ImmutableSortedMap.<Integer, String>naturalOrder()
                    .put(1, "генерал - фельдмаршал")
                    .put(2, "генерал")
                    .put(3, "генерал-лейтенант")
                    .put(4, "генерал-майор")
                    .put(6, "полковник")
                    .put(7, "подполковник")
                    .put(8, "майор")
                    .put(9, "капитан")
                    .put(10, "штабс-капитан")
                    .put(11, "поручик")
                    .put(12, "подпоручик")
                    .put(13, "прапорщик")
                    .build(),

            CAVALRY, ImmutableSortedMap.<Integer, String>naturalOrder()
                    .put(6, "полковник")
                    .put(7, "подполковник")
                    .put(8, "майор")
                    .put(9, "ротмистр")
                    .put(10, "штабс-ротмистр")
                    .put(11, "поручик")
                    .put(13, "корнет")
                    .build(),

            FLEET, ImmutableSortedMap.<Integer, String>naturalOrder()
                    .put(1, "генерал-адмирал")
                    .put(2, "адмирал")
                    .put(4, "контр-адмирал")
                    .put(5, "капитан-командор")
                    .put(6, "капитан 1-го ранга")
                    .put(7, "капитан 2-го ранга")
                    .put(8, "капитан 3-го ранга")
                    .put(9, "капитан-лейтенант")
                    .put(10, "лейтенант")
                    .put(13, "мичман")
                    .build(),

            GUARDS_INFANTRY, ImmutableSortedMap.<Integer, String>naturalOrder()
                    .put(6, "полковник")
                    .put(7, "капитан")
                    .put(8, "штабс-капитан")
                    .put(9, "поручик")
                    .put(10, "подпоручик")
                    .put(11, "прапорщик")
                    .build(),

            GUARDS_CAVALRY, ImmutableSortedMap.<Integer, String>naturalOrder()
                    .put(6, "полковник")
                    .put(7, "капитан")
                    .put(8, "штабс-ротмистр")
                    .put(9, "поручик")
                    .put(10, "подпоручик")
                    .put(11, "корнет")
                    .build()
    );

    public static String militaryRank(int grade, MilitaryBranch branch) {
        final NavigableMap<Integer, String> ranks = MILITARY_RANKS.get(branch);

        return ranks.get(firstNonNull(ranks.floorKey(grade), ranks.firstKey()));
    }

    public static String rank(int grade, Career career) {
        if (career == MILITARY) {
            militaryRank(grade, RandomChoice.from(MilitaryBranch.class));
        }
        return career.name() + " " + grade;
    }

    private RankTable() {}
}
