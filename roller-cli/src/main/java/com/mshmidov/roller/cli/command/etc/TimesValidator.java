package com.mshmidov.roller.cli.command.etc;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.util.Optional;

public class TimesValidator implements IValueValidator<Optional<Integer>> {

    @Override
    public void validate(String name, Optional<Integer> value) throws ParameterException {
        if (value.orElse(1) <= 0) {
            throw new ParameterException(String.format("Should be positive number (\"%s\")", name));
        }
    }
}
