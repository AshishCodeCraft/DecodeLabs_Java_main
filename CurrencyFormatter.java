package com.decodelabs.currencyconverter;

import java.math.BigDecimal;

/**
 * "The Exhaust: Financial Polish" + "Deconstructing the Formatter".
 *
 * Raw BigDecimal output exposes floating-point-style messiness. Enterprise
 * financial software enforces a strict, uniform presentation: thousands
 * separators and exactly two decimal places, e.g. 131,445.46 instead of
 * 131445.456000.
 */
public final class CurrencyFormatter {

    private CurrencyFormatter() { }

    /**
     * Formats a monetary BigDecimal as "%,10.2f" would in printf:
     *   - "," -> locale thousands-separators
     *   - "10" -> minimum field width
     *   - ".2f" -> exactly two decimal places
     */
    public static String format(BigDecimal amount, String currencyCode) {
        return String.format("%,10.2f %s", amount, currencyCode);
    }
}
