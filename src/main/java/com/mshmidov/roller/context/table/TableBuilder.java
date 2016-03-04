package com.mshmidov.roller.context.table;

import com.mshmidov.roller.model.Range;
import com.mshmidov.roller.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;

import java.util.Optional;

public interface TableBuilder {

    void addRow(Optional<Range> range, String value);

    Optional<Table> build(Optional<DiceExpression> roll);
}
