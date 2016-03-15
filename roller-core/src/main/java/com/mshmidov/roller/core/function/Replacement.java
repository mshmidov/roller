package com.mshmidov.roller.core.function;

import java.util.function.Function;
import java.util.regex.Pattern;

public final class Replacement {

    private static final Pattern SUBCOMMAND = Pattern.compile("\\{(.+)\\}");
    private static final RegexReplacer SUBCOMMAND_REPLACER = new RegexReplacer(SUBCOMMAND, 1);

    private Replacement() {}


    public static Pattern subcommandPattern() {
        return SUBCOMMAND;
    }

    public static RegexReplacer subcommandReplacer() {
        return SUBCOMMAND_REPLACER;
    }

    public static String replaceSubcommands(String s, Function<String, Object> executeSubcommand) {
        return SUBCOMMAND_REPLACER.replace(s, executeSubcommand.andThen(Rendering::renderCommandResult));
    }

}
