package com.mshmidov.roller.context;

import static com.google.common.base.Preconditions.checkState;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class CurrentContext {

    private Optional<InteractiveContext> context = Optional.empty();

    public Optional<InteractiveContext> get() {
        return context;
    }

   public void start(InteractiveContext newContext) {
       checkState(!context.isPresent(), "another context is present");
       context = Optional.of(newContext);
   }

    public void finish() {
        checkState(context.isPresent(), "no context is present");
        context.ifPresent(InteractiveContext::done);
        context = Optional.empty();
    }

}
