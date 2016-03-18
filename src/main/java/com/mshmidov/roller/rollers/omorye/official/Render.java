package com.mshmidov.roller.rollers.omorye.official;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.roller.data.omorye.MilitaryBranch;
import com.mshmidov.roller.rollers.omorye.Descent;

import java.util.Map;

final class Render {

    private Render() {}

    private static final Map<Descent, String> descentRenders = ImmutableMap.of(
            Descent.PEASANT, "крестьянского",
            Descent.BURGHER, "мещанского",
            Descent.MERCHANT, "купеческого",
            Descent.NOBLE, "дворянского",
            Descent.BOYAR, "боярского"
    );

    private static final Map<MilitaryBranch, String> militaryBranchRenders = ImmutableMap.of(
            MilitaryBranch.INFANTRY, "%s в пехоте",
            MilitaryBranch.FLEET, "флотский %s",
            MilitaryBranch.CAVALRY, "%s кавалерии",
            MilitaryBranch.GUARDS_INFANTRY, "гвардии %s",
            MilitaryBranch.GUARDS_CAVALRY, "%s гвардейской кавалерии"
    );

    private static final Map<MilitaryBranch, String> militaryBranchCivilianRenders = ImmutableMap.of(
            MilitaryBranch.INFANTRY, "гражданский чиновник в звании %s пехоты",
            MilitaryBranch.FLEET, "гражданский чиновник в звании %s флота",
            MilitaryBranch.CAVALRY, "гражданский чиновник в звании %s кавалерии"
    );

    static String renderDescentGenitive(Descent descent) {
        return descentRenders.get(descent);
    }

    static String renderMilitaryRank(MilitaryBranch branch, String rank, boolean civilian) {


        return String.format(
                civilian ? militaryBranchCivilianRenders.get(branch) : militaryBranchRenders.get(branch),
                rank);
    }
}
