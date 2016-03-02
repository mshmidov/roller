package com.mshmidov.roller;

import com.mshmidov.roller.shell.RollerBootstrap;
import com.wandrell.tabletop.dice.parser.DiceExpressionParser;
import com.wandrell.tabletop.dice.roller.DefaultRoller;
import com.wandrell.tabletop.dice.roller.Roller;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan
public class RollerApplication {

    public static void main(String[] args) throws IOException {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RollerApplication.class)) {
            RollerBootstrap.main(args, context);
        }
    }

    @Bean
    public DiceExpressionParser diceExpressionParser() {
        return new DiceExpressionParser();
    }

    @Bean
    public Roller roller() {
        return new DefaultRoller();
    }

}
