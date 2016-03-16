package com.mshmidov.roller.core.function;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.function.Function;

public final class Replacement {

    private Replacement() {}


    public static String replaceSubcommands(String s, Function<String, Object> executeSubcommand) {
        final StrSubstitutor strSubstitutor = commandSubstitutor(executeSubcommand);
        return strSubstitutor.replace(s);
    }

    public static StrSubstitutor commandSubstitutor(Function<String, Object> executeSubcommand) {
        final LambdaLookup variableResolver = new LambdaLookup(executeSubcommand.andThen(Rendering::renderCommandResult));
        final StrSubstitutor strSubstitutor = new StrSubstitutor(variableResolver, "{", "}", '\\');
        strSubstitutor.setEnableSubstitutionInVariables(true);
        return strSubstitutor;
    }

    private static class LambdaLookup extends StrLookup<String> {

        private final Function<String, String> lookupFunction;

        private LambdaLookup(Function<String, String> lookupFunction) {
            this.lookupFunction = lookupFunction;
        }

        @Override
        public String lookup(String key) {
            return lookupFunction.apply(key);
        }
    }
}
