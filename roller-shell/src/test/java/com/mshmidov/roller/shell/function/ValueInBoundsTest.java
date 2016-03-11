package com.mshmidov.roller.shell.function;

import static org.junit.Assert.*;

import com.mshmidov.roller.core.function.Functions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ValueInBoundsTest {

    @Test
    public void shouldReturnLowerBoundIfValueIsLess() {

        // given
        final int min = 5;
        final int max = 10;
        final int value = 4;

        // when
        final int result = Functions.valueInBounds(min, max).apply(value);

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
        final int result = Functions.valueInBounds(min, max).apply(value);

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
        final int result = Functions.valueInBounds(min, max).apply(value);

        // then
        assertEquals(value, result);
    }
}
