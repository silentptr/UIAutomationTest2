package com.phantasment.uiautomationtest2.util;

import java.util.Optional;

public final class NumberChecker
{
    private NumberChecker() { }

    public static Optional<Double> isDouble(String str)
    {
        try
        {
            double value = Double.parseDouble(str);
            return Optional.of(value);
        }
        catch (Throwable t)
        {
            return Optional.empty();
        }
    }
}
