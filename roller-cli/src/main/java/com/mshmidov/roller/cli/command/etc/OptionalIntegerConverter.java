package com.mshmidov.roller.cli.command.etc;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.util.Optional;

public class OptionalIntegerConverter implements IStringConverter<Optional<Integer>> {

    @Override
    public Optional<Integer> convert(String value) {
        if (isBlank(value)) {
            return Optional.empty();

        } else {
            try {
                return Optional.of(Integer.parseInt(value));

            } catch (NumberFormatException e) {
                throw new ParameterException(e.getMessage());
            }
        }
    }
}
