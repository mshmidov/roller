package com.mshmidov.roller.core.service;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.mshmidov.roller.core.model.Range;
import com.mshmidov.roller.core.model.Table;
import com.wandrell.tabletop.dice.notation.DiceExpression;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class TableLoader {

    private static final Logger logger = LoggerFactory.getLogger(TableLoader.class);

    private static final Splitter FIRST_SPACE_SPLITTER = Splitter.on(' ').limit(2).omitEmptyStrings();

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+)(?:-|\\.\\.)(\\d+)");

    private final DiceExpressionParser diceExpressionParser;

    public TableLoader(DiceExpressionParser diceExpressionParser) {
        this.diceExpressionParser = diceExpressionParser;
    }

    public Optional<Table> loadTable(File file) {

        try {

            final LinkedList<String> lines = new LinkedList<>(Files.lines(file.toPath())
                    .filter(line -> !StringUtils.startsWithAny(line, "//", ";", "#"))
                    .collect(Collectors.toList()));

            if (lines.isEmpty()) {
                logger.warn("Table file {} is empty and will not be processed", file);

            } else {

                return createTable(StringUtils.removeEnd(file.getName(), ".table"), lines);
            }


        } catch (IOException e) {
            logger.error("Error loading table from file " + file.toString(), e);
        }

        return Optional.empty();
    }


    public Optional<Table> createTable(String name, Collection<String> definition) {
        final LinkedList<String> lines = new LinkedList<>(definition);

        final TableBuilder tableBuilder = new TableBuilder(name);

        final Optional<DiceExpression> dice = getPotentialDiceExpression(lines.getFirst()).flatMap(this::parseDiceExpression);

        if (dice.isPresent()) {
            lines.removeFirst();
        }

        lines.stream().map(this::parseRow).forEach(o -> o.ifPresent(p -> tableBuilder.addRow(p.getKey(), p.getValue())));

        return tableBuilder.build(dice);
    }

    private Optional<String> getPotentialDiceExpression(String firstLine) {
        final List<String> parts = FIRST_SPACE_SPLITTER.splitToList(firstLine);

        if (parts.size() == 2 && ImmutableSet.of("roll", "dice").contains(parts.get(0).toLowerCase())) {
            return Optional.of(parts.get(1));
        }

        return Optional.empty();
    }

    private Optional<DiceExpression> parseDiceExpression(String potentialDiceExpression) {
        try {
            return Optional.of(diceExpressionParser.parse(potentialDiceExpression));

        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    private Optional<Pair<Optional<Range>, String>> parseRow(String line) {

        final LinkedList<String> parts = new LinkedList<>(FIRST_SPACE_SPLITTER.splitToList(line));

        if (parts.isEmpty()) {
            return Optional.empty();
        }

        final Optional<Range> range = (parts.size() == 2)
                ? parseRange(parts.poll())
                : Optional.empty();

        return Optional.of(Pair.of(
                range,
                range.map(r -> parts.getFirst()).orElse(line)));
    }


    private Optional<Range> parseRange(String string) {
        try {
            final int v = Integer.parseInt(string);

            return Optional.of(new Range(v, v));

        } catch (NumberFormatException ignored) {
            final Matcher matcher = RANGE_PATTERN.matcher(string);
            if (matcher.matches()) {
                final int lower = Integer.parseInt(matcher.group(1));
                final int upper = Integer.parseInt(matcher.group(2));

                return Optional.of(new Range(lower, upper));
            }
        }

        return Optional.empty();
    }
}
