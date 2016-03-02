package com.mshmidov.roller.function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BoundedTest {

    @Test
    public void shouldReturnLowerBoundIfValueIsLess() {

        // given
        final int min = 5;
        final int max = 10;
        final int value = 4;

        // when
        final int result = new Bounded(min, max).apply(value);

        // then
        assertEquals(min, result);
    }

    @Test
    public void shouldReturnUpperBoundIfValueIsMore() {

        // given
        final int min = 5;
        final int max = 10;
        final int value = 11;

        // when
        final int result = new Bounded(min, max).apply(value);

        // then
        assertEquals(max, result);
    }

    @Test
    public void shouldReturnValueIfItIsInBounds() {

        // given
        final int min = 5;
        final int max = 10;
        final int value = 7;

        // when
        final int result = new Bounded(min, max).apply(value);

        // then
        assertEquals(value, result);
    }
}
