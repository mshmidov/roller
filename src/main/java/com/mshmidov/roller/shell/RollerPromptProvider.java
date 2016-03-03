package com.mshmidov.roller.shell;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.context.InteractiveContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public final class RollerPromptProvider implements PromptProvider {

    @Autowired private CurrentContext currentContext;

    @Override
    public String getPrompt() {
        final String context = currentContext.getInteractiveContext().map(InteractiveContext::getPrompt).orElse("roller");

        return String.format("%s>", context);
    }

    @Override
    public String getProviderName() {
        return this.getClass().getSimpleName();
    }
}
