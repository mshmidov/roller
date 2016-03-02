package com.mshmidov.roller.shell;

import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.Completion;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.MethodTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiceExpressionConverter implements Converter<DiceExpression> {

	@Autowired private DiceExpressionParser diceExpressionParser;

	@Override
	public DiceExpression convertFromText(final String value, final Class<?> requiredType, final String optionContext) {
		return diceExpressionParser.parse(value);
	}

	@Override
	public boolean getAllPossibleValues(final List<Completion> completions, final Class<?> requiredType,
			final String existingData, final String optionContext, final MethodTarget target) {
		return false;
	}

	@Override
	public boolean supports(final Class<?> requiredType, final String optionContext) {
		return DiceExpression.class.isAssignableFrom(requiredType);
	}
}
