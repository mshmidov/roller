package com.mshmidov.roller.rollers.omorye.official;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.roller.rollers.omorye.Descent;

import java.util.Map;

final class Render {

    private Render() {}

    private static final Map<Descent, String> descentRenders = ImmutableMap.of(
            Descent.PEASANT, "крестьянского",
            Descent.BURGHER, "мещанского",
            Descent.MERCHANT, "купеческого",
            Descent.NOBLE, "дворянского",
            Descent.BOYAR, "боярского"
    );

    static String renderDescentGenitive(Descent descent) {
        return descentRenders.get(descent);
    }

}
