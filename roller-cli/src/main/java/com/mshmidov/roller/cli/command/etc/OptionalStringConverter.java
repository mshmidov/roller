package com.mshmidov.roller.cli.command.etc;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.beust.jcommander.IStringConverter;

import java.util.Optional;

public class OptionalStringConverter implements IStringConverter<Optional<String>> {

    @Override
    public Optional<String> convert(String value) {
        return isBlank(value) ? Optional.empty() : Optional.of(value);
    }
}
