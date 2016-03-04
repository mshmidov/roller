package com.mshmidov.roller.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SideEffect<T> implements Function<T, T> {

    private final Consumer<T> effect;

    public SideEffect(Consumer<T> effect) {
        this.effect = effect;
    }

    @Override
    public T apply(T value) {
        effect.accept(value);
        return value;
    }

    public static <T> SideEffect<T> sideEffect(Consumer<T> effect) {
        return new SideEffect<>(effect);
    }

    public static <T> Supplier<T> sideEffect(Action effect, T value) {
        return () -> {
            effect.perform();
            return value;
        };
    }
}
