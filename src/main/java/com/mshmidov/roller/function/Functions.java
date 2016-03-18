package com.mshmidov.roller.function;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.notation.DiceExpressionComponent;
import com.wandrell.tabletop.dice.notation.operation.Operand;
import com.wandrell.tabletop.dice.notation.operation.Operation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public final class Functions {

    private Functions() {
    }

    public static int rollDice(DiceExpression dice) {
        final DiceExpressionComponent component = Iterables.getOnlyElement(dice.getComponents());

        if (component instanceof Operation) {
            return ((Operation) component).operate().getValue();
        }

        if (component instanceof Operand) {
            return ((Operand) component).getValue();
        }

        throw new IllegalStateException();
    }

    public static IntSupplier diceRollSupplier(DiceExpression dice) {
        return () -> rollDice(dice);
    }

    public static IntToIntFunction valueInBounds(int min, int max) {
        return i -> Math.max(Math.min(i, max), min);
    }

    public static Path getResource(Class<?> clazz, String resourceName) {
        try {
            final URI uri = clazz.getResource(resourceName).toURI();

            try {
                FileSystems.getFileSystem(uri);

            } catch (FileSystemNotFoundException e) {
                FileSystems.newFileSystem(uri, ImmutableMap.of("create", "true"));
            }

            return Paths.get(uri);

        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Cannot load resource", e);
        }
    }

    public static List<Pair<Integer, String>> loadRanks(Path resource) {
        try {
            return Files.lines(resource)
                    .filter(StringUtils::isNotBlank)
                    .map(Functions::parseRank)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load ranks", e);
        }
    }

    private static Pair<Integer, String> parseRank(String s) {
        final String[] parts = s.split("\\s", 2);
        if (parts.length == 2) {
            return Pair.of(Integer.valueOf(parts[0]), parts[1]);

        } else {
            throw new IllegalArgumentException("Incorrect rank string: " + s);
        }
    }

    public static <K extends Comparable<K>, V> NavigableMap<K, V> toNavigableMap(Collection<Pair<K, V>> pairs) {
        final TreeMap<K, V> result = new TreeMap<>();

        pairs.forEach(pair -> result.put(pair.getKey(), pair.getValue()));

        return result;
    }
}
