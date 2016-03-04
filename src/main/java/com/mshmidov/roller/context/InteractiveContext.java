package com.mshmidov.roller.context;

import java.util.Optional;

public interface InteractiveContext<T> {

    String getPrompt();

    Optional<T> done();
}
