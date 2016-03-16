package com.mshmidov.roller.core.function;

import ch.qos.logback.classic.Level;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.notation.DiceExpressionComponent;
import com.wandrell.tabletop.dice.notation.operation.Operand;
import com.wandrell.tabletop.dice.notation.operation.Operation;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.IntSupplier;

public final class Functions {

    private static final Splitter DEFINITION_SPLITTER = Splitter.on(';').omitEmptyStrings().trimResults();

    private Functions() {}

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

    public static void enableDebugOutput(String loggerName) {
        final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(Level.DEBUG);
    }

    public static void disableDebugOutput(String loggerName) {
        final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(Level.ERROR);
    }

    public static List<String> splitDefinition(String definition) {
        return DEFINITION_SPLITTER.splitToList(definition);
    }

}
