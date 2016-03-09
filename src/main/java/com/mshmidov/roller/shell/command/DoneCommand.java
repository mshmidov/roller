package com.mshmidov.roller.shell.command;

import com.mshmidov.roller.context.CurrentContext;
import com.mshmidov.roller.service.TableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

@Component
public class DoneCommand implements CommandMarker {

    private static final Logger logger = LoggerFactory.getLogger(DoneCommand.class);

    public static final String KEYWORD = "done";

    @Autowired CurrentContext context;

    @Autowired TableRegistry tableRegistry;

    @CliAvailabilityIndicator(value = KEYWORD)
    public boolean isAvailable() {
        return true;
    }

    @CliCommand(value = KEYWORD, help = "finishes current context")
    public void execute() {
        if (context.getInteractiveContext().isPresent()) {
            context.finishInteractiveContext().map(String::valueOf).ifPresent(logger::info);
        }
    }
}
