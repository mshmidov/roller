package com.mshmidov.roller.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.ExecutionProcessor;
import org.springframework.shell.core.ExecutionStrategy;
import org.springframework.shell.event.ParseResult;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class RollerExecutionStrategy implements ExecutionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RollerExecutionStrategy.class);

    private final Class<?> mutex = RollerExecutionStrategy.class;

    public Object execute(ParseResult parseResult) throws RuntimeException {
        Assert.notNull(parseResult, "Parse result required");
        synchronized (mutex) {
            Assert.isTrue(isReadyForCommands(), "RollerExecutionStrategy not yet ready for commands");
            Object target = parseResult.getInstance();
            if (target instanceof ExecutionProcessor) {
                ExecutionProcessor processor = ((ExecutionProcessor) target);
                parseResult = processor.beforeInvocation(parseResult);
                try {
                    Object result = invoke(parseResult);
                    processor.afterReturningInvocation(parseResult, result);
                    return result;
                } catch (Throwable th) {
                    processor.afterThrowingInvocation(parseResult, th);
                    return handleThrowable(th);
                }
            } else {
                return invoke(parseResult);
            }
        }
    }

    private Object invoke(ParseResult parseResult) {
        try {
            return ReflectionUtils.invokeMethod(parseResult.getMethod(), parseResult.getInstance(), parseResult.getArguments());
        } catch (Throwable th) {
            logger.error("Command failed " + th);
            logger.debug("Cause:", th);
            return null;
        }
    }

    private Object handleThrowable(Throwable th) {
        if (th instanceof Error) {
            throw ((Error) th);
        }
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        }
        throw new RuntimeException(th);
    }

    public boolean isReadyForCommands() {
        return true;
    }

    public void terminate() {
        // do nothing
    }
}
