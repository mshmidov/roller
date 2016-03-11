package com.mshmidov.roller.shell.converter;

import com.mshmidov.roller.core.model.Range;
import org.springframework.shell.core.Completion;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.MethodTarget;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RangeConverter implements Converter<Range> {

    public static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+)(?:-|\\.\\.)(\\d+)");

    @Override
    public Range convertFromText(final String value, final Class<?> requiredType, final String optionContext) {
        try {
            final int v = Integer.parseInt(value);

            return new Range(v, v);

        } catch (NumberFormatException ignored) {
            final Matcher matcher = RANGE_PATTERN.matcher(value);
            if (matcher.matches()) {
                final int lower = Integer.parseInt(matcher.group(1));
                final int upper = Integer.parseInt(matcher.group(2));

                return new Range(lower, upper);
            }
        }

        return null;
    }

    @Override
    public boolean getAllPossibleValues(final List<Completion> completions, final Class<?> requiredType,
            final String existingData, final String optionContext, final MethodTarget target) {
        return false;
    }

    @Override
    public boolean supports(final Class<?> requiredType, final String optionContext) {
        return Range.class.isAssignableFrom(requiredType);
    }
}
