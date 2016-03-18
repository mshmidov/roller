package com.mshmidov.roller.rollers.omorye.official;


import static com.mshmidov.roller.data.omorye.Career.MILITARY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.CAVALRY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.FLEET;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.GUARDS_CAVALRY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.GUARDS_INFANTRY;
import static com.mshmidov.roller.data.omorye.MilitaryBranch.INFANTRY;
import static com.mshmidov.roller.rollers.Sex.MALE;
import static com.mshmidov.roller.rollers.omorye.Descent.BOYAR;
import static com.mshmidov.roller.rollers.omorye.Descent.BURGHER;
import static com.mshmidov.roller.rollers.omorye.Descent.MERCHANT;
import static com.mshmidov.roller.rollers.omorye.Descent.NOBLE;
import static com.mshmidov.roller.rollers.omorye.Descent.PEASANT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.roller.data.omorye.Career;
import com.mshmidov.roller.data.omorye.MilitaryBranch;
import com.mshmidov.roller.data.omorye.Names;
import com.mshmidov.roller.data.omorye.RankTable;
import com.mshmidov.roller.function.RandomChoice;
import com.mshmidov.roller.function.TableBuilder;
import com.mshmidov.roller.function.TableRegistry;
import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.mshmidov.roller.rollers.omorye.Descent;

import java.util.Map;
import java.util.Optional;

public class OfficialRoller {

    private final TableRegistry tableRegistry = TableRegistry.newInstance();
    private final Names names = new Names(tableRegistry);

    private final Table<Descent> randomDescent = new TableBuilder<Descent>("descent")
            .row(new Range(1), PEASANT)
            .row(new Range(2, 4), BURGHER)
            .row(new Range(5, 6), MERCHANT)
            .row(new Range(7, 9), NOBLE)
            .row(new Range(10), BOYAR)
            .build(Optional.empty()).orElseThrow(IllegalStateException::new);

    private final Table<Integer> randomGrade = new TableBuilder<Integer>("grade")
            .row(new Range(1000, 1000), 1)
            .row(new Range(998, 999), 2)
            .row(new Range(995, 997), 3)
            .row(new Range(990, 994), 4)
            .row(new Range(982, 989), 5)
            .row(new Range(969, 981), 6)
            .row(new Range(948, 968), 7)
            .row(new Range(914, 947), 8)
            .row(new Range(859, 913), 9)
            .row(new Range(770, 858), 10)
            .row(new Range(626, 769), 11)
            .row(new Range(393, 625), 12)
            .row(new Range(16, 392), 13)
            .build(Optional.empty()).orElseThrow(IllegalStateException::new);

    private final Table<MilitaryBranch> randomMilitaryBranch = new TableBuilder<MilitaryBranch>("militaryBranch")
            .row(new Range(1, 3), INFANTRY)
            .row(new Range(4, 6), FLEET)
            .row(new Range(7, 8), CAVALRY)
            .row(new Range(9), GUARDS_CAVALRY)
            .row(new Range(10), GUARDS_INFANTRY)
            .build(Optional.empty()).orElseThrow(IllegalStateException::new);

    private final Map<Descent, Integer> descentGradeModifier = ImmutableMap.of(
            PEASANT, 0,
            BURGHER, +50,
            MERCHANT, +100,
            NOBLE, +150,
            BOYAR, +200);

    private final Map<Descent, Integer> descentMilitaryBranchModifier = ImmutableMap.of(
            PEASANT, 0,
            BURGHER, 0,
            MERCHANT, 0,
            NOBLE, +3,
            BOYAR, +6);

    private final Map<Descent, Double> descentTitleChance = ImmutableMap.of(
            PEASANT, 0d,
            BURGHER, 0d,
            MERCHANT, 0d,
            NOBLE, 0.2,
            BOYAR, 0.3);

    public void roll() {

        final Descent descent = randomDescent.rollValue();
        final String name = names.randomName(MALE) + " " + names.randomSurname(descent, MALE);
        final String title = RandomChoice.byChance(descentTitleChance.get(descent), RandomChoice.from("граф ", "князь ")).orElse("");

        final int gradeModifier = descentGradeModifier.get(descent) + (isNotBlank(title) ? 200 : 0);
        final int grade = this.randomGrade.rollValue(gradeModifier);

        final int age = RandomChoice.between(16 + (13 - grade) * 3, 65 - (grade * 2));

        final Career career = RandomChoice.from(Career.class);

        final String rank;

        if (career == MILITARY) {
            final MilitaryBranch militaryBranch = randomMilitaryBranch.rollValue(descentMilitaryBranchModifier.get(descent));

            final boolean civilian = militaryBranch != GUARDS_CAVALRY && militaryBranch != GUARDS_INFANTRY && RandomChoice.byChance(0.2);

            rank = Render.renderMilitaryRank(militaryBranch, RankTable.militaryRank(grade, militaryBranch), civilian);
        } else {
            rank = RankTable.rank(grade, career);
        }

        System.out.println(String.format("%s%s, %d, %s рода, %s",
                title, name, age, Render.renderDescentGenitive(descent), rank));
    }

}
