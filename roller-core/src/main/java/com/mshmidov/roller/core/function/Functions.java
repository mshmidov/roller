package com.mshmidov.roller.core.function;

import ch.qos.logback.classic.Level;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.mshmidov.roller.core.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.notation.DiceExpressionComponent;
import com.wandrell.tabletop.dice.notation.operation.Operand;
import com.wandrell.tabletop.dice.notation.operation.Operation;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Functions {

    private static final Pattern SUBCOMMAND = Pattern.compile("\\{(.+)\\}");

    private static final Splitter DEFINITION_SPLITTER = Splitter.on(';').omitEmptyStrings().trimResults();

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
        return replaceRegex(source, pattern, 0, replacement);
    }

    public static String replaceRegex(String source, Pattern pattern, int group, Function<String, String> replacement) {
        final StringBuffer result = new StringBuffer();

        final Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            matcher.appendReplacement(result, replacement.apply(matcher.group(group)));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public static void enableDebugOutput(String loggerName) {
        final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(Level.DEBUG);
    }

    public static void disableDebugOutput(String loggerName) {
        final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(Level.OFF);
    }

    public static Pattern subcommandPattern() {
        return SUBCOMMAND;
    }

    public static List<String> splitDefinition(String definition) {
        return DEFINITION_SPLITTER.splitToList(definition);
    }

}
