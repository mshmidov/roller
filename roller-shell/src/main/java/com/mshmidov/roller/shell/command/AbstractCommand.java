package com.mshmidov.roller.shell.command;

import static com.mshmidov.roller.core.function.Functions.disableDebugOutput;
import static com.mshmidov.roller.core.function.Functions.enableDebugOutput;
import static com.mshmidov.roller.core.function.Replacement.replaceSubcommands;

import com.mshmidov.roller.shell.RollerJLineShellComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.ExecutionProcessor;
import org.springframework.shell.event.ParseResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCommand implements ExecutionProcessor {

    @Autowired(required = false) protected RollerJLineShellComponent shell;

    @Override
    public final ParseResult beforeInvocation(ParseResult invocationContext) {
        if (isVerbose(invocationContext)) {
            enableDebugOutput("com.mshmidov.roller");
            enableDebugOutput("org.springframework.shell");
        }

        findArgumentsByAnnotation(invocationContext.getMethod().getParameterAnnotations(), Expand.class).stream()
                .filter(i -> invocationContext.getMethod().getParameterTypes()[i] == String.class)
                .filter(i -> invocationContext.getArguments()[i] != null)
                .forEach(i -> invocationContext.getArguments()[i] = expandArgument(String.valueOf(invocationContext.getArguments()[i])));

        return invocationContext;
    }

    @Override
    public final void afterReturningInvocation(ParseResult invocationContext, Object result) {
        if (isVerbose(invocationContext)) {
            disableDebugOutput("com.mshmidov.roller");
            disableDebugOutput("org.springframework.shell");
        }
    }

    @Override
    public final void afterThrowingInvocation(ParseResult invocationContext, Throwable thrown) {
        if (isVerbose(invocationContext)) {
            disableDebugOutput("com.mshmidov.roller");
            disableDebugOutput("org.springframework.shell");
        }
    }

    private Optional<Integer> findFirstArgumentByAnnotation(Annotation[][] parameterAnnotations, Class<? extends Annotation> annotationClass) {
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    return Optional.of(i);
                }
            }
        }
        return Optional.empty();
    }

    private List<Integer> findArgumentsByAnnotation(Annotation[][] parameterAnnotations, Class<? extends Annotation> annotationClass) {
        final List<Integer> result = new ArrayList<>(parameterAnnotations.length);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    result.add(i);
                }
            }
        }
        return result;
    }


    protected boolean isVerbose(ParseResult invocationContext) {
        return findFirstArgumentByAnnotation(invocationContext.getMethod().getParameterAnnotations(), Verbose.class)
                .map(i -> invocationContext.getArguments()[i])
                .map(arg -> Boolean.valueOf(String.valueOf(arg)))
                .orElse(false);
    }

    protected String expandArgument(String s) {
        return replaceSubcommands(s, subcommand -> shell.executeNonInteractiveCommand(subcommand).getResult());
    }

}
