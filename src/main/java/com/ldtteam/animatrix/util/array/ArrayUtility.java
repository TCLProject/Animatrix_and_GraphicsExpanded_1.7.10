package com.ldtteam.animatrix.util.array;

import java.util.function.Supplier;

public final class ArrayUtility
{

    private ArrayUtility()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static <T> void InitializeArray(final T[] array, final Supplier<T> supplier)
    {
        for (int i = 0; i < array.length; i++)
        {
            array[i] = supplier.get();
        }
    }
}