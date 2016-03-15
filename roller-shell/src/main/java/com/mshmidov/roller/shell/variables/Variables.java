package com.mshmidov.roller.shell.variables;

import static java.util.Objects.requireNonNull;

import com.mshmidov.roller.core.error.IncorrectVariableNameException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public final class Variables {

    public static final Pattern ILLEGAL_SYMBOLS = Pattern.compile("\\W");

    private final Map<String, String> values = new HashMap<>();

    public String set(String name, String value) {

        if (name == null || Variables.ILLEGAL_SYMBOLS.matcher(name).find()) {
            throw new IncorrectVariableNameException("Incorrect variable name: " + name);
        }

        return values.put(requireNonNull(name), value);
    }

    public Optional<String> get(String name) {
        return Optional.ofNullable(values.get(name));
    }

    public Map<String, String> getAll() {
        return Collections.unmodifiableMap(values);
    }

    public String remove(String name) {
        return values.remove(name);
    }
}
