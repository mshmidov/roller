package com.mshmidov.roller.shell;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public final class RollerPromptProvider implements PromptProvider {

    @Override
    public String getPrompt() {
        return "roller>";
    }

    @Override
    public String getProviderName() {
        return this.getClass().getSimpleName();
    }
}
