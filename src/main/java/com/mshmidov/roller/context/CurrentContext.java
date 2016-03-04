package com.mshmidov.roller.context;

import static com.google.common.base.Preconditions.checkState;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class CurrentContext {

    private Optional<InteractiveContext> context = Optional.empty();

    public Optional<InteractiveContext> getInteractiveContext() {
        return context;
    }

   public void startInteractiveContext(InteractiveContext newContext) {
       checkState(!context.isPresent(), "another context is present");
       context = Optional.of(newContext);
   }

    public Optional<?> finishInteractiveContext() {
        checkState(context.isPresent(), "no context is present");
        final Optional<?> result = context.flatMap(InteractiveContext::done);
        context = Optional.empty();
        return result;
    }

}
