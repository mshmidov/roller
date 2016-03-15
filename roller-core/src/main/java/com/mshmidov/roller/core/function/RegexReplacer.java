package com.mshmidov.roller.core.function;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexReplacer {

    private final Pattern pattern;
    private final int group;

    public RegexReplacer(Pattern pattern, int group) {
        this.pattern = pattern;
        this.group = group;
    }

    public String replace(String s, Function<String, String> replacement) {
        final StringBuffer result = new StringBuffer();

        final Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            matcher.appendReplacement(result, replacement.apply(matcher.group(group)));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
