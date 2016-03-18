package com.mshmidov.roller.data.omorye;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.roller.function.RandomChoice;
import com.mshmidov.roller.model.Range;

import java.util.Map;
import java.util.NavigableMap;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.mshmidov.roller.data.omorye.Career.MILITARY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.*;
import static com.mshmidov.roller.function.Functions.*;

public final class RankTable {

    public static final Range GRADES = new Range(1, 13);

    private static final Map<MilitaryBranch, NavigableMap<Integer, String>> MILITARY_RANKS = ImmutableMap.of(
            INFANTRY, toNavigableMap(loadRanks(getResource(RankTable.class, "rank-military-infantry"))),
            CAVALRY, toNavigableMap(loadRanks(getResource(RankTable.class, "rank-military-cavalry"))),
            FLEET, toNavigableMap(loadRanks(getResource(RankTable.class, "rank-military-fleet"))),
            GUARDS_INFANTRY, toNavigableMap(loadRanks(getResource(RankTable.class, "rank-military-guards-infantry"))),
            GUARDS_CAVALRY, toNavigableMap(loadRanks(getResource(RankTable.class, "rank-military-guards-cavalry")))
    );

    public static String militaryRank(int grade, MilitaryBranch branch) {
        final NavigableMap<Integer, String> ranks = MILITARY_RANKS.get(branch);

        return ranks.get(firstNonNull(ranks.ceilingKey(grade), ranks.lastKey()));
    }

    public static String rank(int grade, Career career) {
        if (career == MILITARY) {
            militaryRank(grade, RandomChoice.from(MilitaryBranch.class));
        }
        return career.name() + " " + grade;
    }

    private RankTable() {
    }
}
