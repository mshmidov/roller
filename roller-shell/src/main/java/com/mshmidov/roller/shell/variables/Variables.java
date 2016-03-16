package com.mshmidov.roller.shell.variables;

import static java.util.Objects.requireNonNull;

import com.mshmidov.roller.core.error.IncorrectVariableNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public final class Variables {

    private static final Logger logger = LoggerFactory.getLogger(Variables.class);

    public static final Pattern ILLEGAL_SYMBOLS = Pattern.compile("\\W");

    private final Map<String, String> values = new HashMap<>();

    public String set(String name, String value) {

        logger.debug("Will set variable {}={}", name, value);

        if (name == null || Variables.ILLEGAL_SYMBOLS.matcher(name).find()) {
            throw new IncorrectVariableNameException("Incorrect variable name: " + name);
        }

        return values.put(requireNonNull(name), value);
    }

    public Optional<String> get(String name) {
        final Optional<String> value = Optional.ofNullable(values.get(name));

        logger.debug("Variable {}{}", name, value.map(s -> "=" + s).orElse(" is unknown"));

        return value;
    }

    public Map<String, String> getAll() {
        return Collections.unmodifiableMap(values);
    }

    public String remove(String name) {
        logger.debug("Will remove variable {}", name);

        return values.remove(name);
    }
}
