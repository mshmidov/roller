package com.mshmidov.roller.function;

import com.google.common.collect.Iterables;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.notation.DiceExpressionComponent;
import com.wandrell.tabletop.dice.notation.operation.Operand;
import com.wandrell.tabletop.dice.notation.operation.Operation;

import java.util.function.Supplier;

public final class RollDice implements Supplier<Integer> {

    private final DiceExpression diceExpression;

    public RollDice(DiceExpression diceExpression) {
        this.diceExpression = diceExpression;
    }

    @Override
    public Integer get() {
        return roll(diceExpression);
    }

    private int roll(DiceExpression dice) {
        final DiceExpressionComponent component = Iterables.getOnlyElement(dice.getComponents());
        if (component instanceof Operation) {
            return ((Operation) component).operate().getValue();
        }
        if (component instanceof Operand) {
            return ((Operand) component).getValue();
        }

        throw new IllegalStateException();
    }

    public static Supplier<Integer> rollDice(DiceExpression diceExpression) {
        return new RollDice(diceExpression);
    }
}
