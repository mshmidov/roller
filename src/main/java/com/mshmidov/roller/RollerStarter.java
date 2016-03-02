package com.mshmidov.roller;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.shell.CommandLine;
import org.springframework.shell.ShellException;
import org.springframework.shell.SimpleShellCommandLineOptions;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.JLineShellComponent;
import org.springframework.shell.support.logging.HandlerUtils;

import java.io.IOException;
import java.util.logging.Logger;


public class RollerStarter {

    public static void main(String[] args) throws IOException {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        final CommandLine commandLine = parseCommandLine(args);

        beanFactory.registerSingleton("commandLine", commandLine);
        beanFactory.registerBeanDefinition("shell", new RootBeanDefinition(JLineShellComponent.class));

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(beanFactory)) {
            context.register(RollerApplication.class);
            context.scan("org.springframework.shell.converters", "org.springframework.shell.plugin.support");

            if (!commandLine.getDisableInternalCommands()) {
                context.scan("org.springframework.shell.commands");
            }

            context.refresh();

            try {

                final ExitShellRequest exitShellRequest = context.getBean(RollerApplication.class).run();

                System.exit(exitShellRequest.getExitCode());

            } catch (RuntimeException t) {
                throw t;
            } finally {
                HandlerUtils.flushAllHandlers(Logger.getLogger(""));
            }
        }
    }

    private static CommandLine parseCommandLine(String[] args) {
        try {
            return SimpleShellCommandLineOptions.parseCommandLine(args);
        } catch (IOException e) {
            throw new ShellException(e.getMessage(), e);
        }
    }
}
