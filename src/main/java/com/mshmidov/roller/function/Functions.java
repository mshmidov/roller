package com.mshmidov.roller.function;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.notation.DiceExpressionComponent;
import com.wandrell.tabletop.dice.notation.operation.Operand;
import com.wandrell.tabletop.dice.notation.operation.Operation;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;

public final class Functions {

    public static final Pattern VALID_TABLE_NAME = Pattern.compile("[\\w-]+");

    private Functions() {}

    public static String tableToString(Table table) {

        final List<String> parts = new ArrayList<>();
        parts.add(table.getName());

        table.getGroupedRows().forEach((range, s) -> parts.add(String.format("%s: %s", range.toString(), s)));

        return Joiner.on('\n').join(parts);
    }

    public static int rollDice(DiceExpression dice) {
        final DiceExpressionComponent component = Iterables.getOnlyElement(dice.getComponents());

        if (component instanceof Operation) {
            return ((Operation) component).operate().getValue();
        }

        if (component instanceof Operand) {
            return ((Operand) component).getValue();
        }

        throw new IllegalStateException();
    }

    public static IntSupplier diceRollSupplier(DiceExpression dice) {
        return () -> rollDice(dice);
    }

    public static IntToIntFunction valueInBounds(int min, int max) {
        return i -> Math.max(Math.min(i, max), min);
    }

    public static boolean tableNameValid(String name) {
        return VALID_TABLE_NAME.matcher(name).matches();
    }

}
