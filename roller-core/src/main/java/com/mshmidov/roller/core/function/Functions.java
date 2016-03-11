package com.mshmidov.roller.core.function;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.mshmidov.roller.core.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.notation.DiceExpressionComponent;
import com.wandrell.tabletop.dice.notation.operation.Operand;
import com.wandrell.tabletop.dice.notation.operation.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Functions {

    private Functions() {}

    public static String tableToString(Table table) {

        final List<String> parts = new ArrayList<>();
        parts.add(table.getName());

        table.getRows().forEach((range, s) -> parts.add(String.format("%s: %s", range.toString(), s)));

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

    public static String replaceRegex(String source, Pattern pattern, Function<String, String> replacement) {
        final StringBuffer result = new StringBuffer();

        final Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            matcher.appendReplacement(result, replacement.apply(matcher.group()));
        }
        matcher.appendTail(result);

        return result.toString();
    }

}
