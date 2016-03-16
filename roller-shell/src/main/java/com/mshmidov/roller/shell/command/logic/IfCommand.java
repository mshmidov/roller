package com.mshmidov.roller.shell.command.logic;

import com.mshmidov.roller.core.error.IncorrectConditionException;
import com.mshmidov.roller.shell.RollerJLineShellComponent;
import com.mshmidov.roller.shell.command.AbstractCommand;
import com.mshmidov.roller.shell.command.Expand;
import com.mshmidov.roller.shell.command.Verbose;
import com.mshmidov.roller.shell.variables.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.BiFunction;

@Component
public class IfCommand extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(IfCommand.class);

    @Autowired private Variables variables;

    @Autowired(required = false) private RollerJLineShellComponent shell;

    @CliCommand(value = "if", help = "evaluate a result based on condition")
    public String execute(
            @Expand @CliOption(key = "", help = "condition") final String condition,
            @Expand @CliOption(key = "then", mandatory = true) String thenValue,
            @Expand @CliOption(key = "else", unspecifiedDefaultValue = "") String elseValue,
            @Verbose @CliOption(key = { "verbose", "v" }, help = "enable debug output", specifiedDefaultValue = "true", unspecifiedDefaultValue = "false")
            boolean verbose) {

        logger.debug("if {} then {} else {}", condition, thenValue, elseValue);

        for (Operator operator : Operator.values()) {
            if (condition.contains(operator.operator)){
                final String[] operands = condition.split(operator.operator, 2);
                if (operands.length != 2) {
                    throw new IncorrectConditionException("Incorrect condition: " + condition);
                }
                if (operator.predicate.apply(operands[0], operands[1])) {
                    return thenValue;
                } else {
                    return elseValue;
                }
            }
        }

        throw new IncorrectConditionException("Incorrect condition: " + condition);
    }

    public enum Operator {

        EQUALS("=", Objects::equals),
        NOT_EQUALS("<>", (a, b) -> !Objects.equals(a, b));

        final String operator;
        final BiFunction<String, String, Boolean> predicate;

        Operator(String operator, BiFunction<String, String, Boolean> predicate) {
            this.operator = operator;
            this.predicate = predicate;
        }
    }
}
