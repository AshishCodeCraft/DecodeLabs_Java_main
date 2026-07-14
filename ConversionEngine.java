package com.decodelabs.currencyconverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * "The Combustion Engine: Exchange Routing" + "The Crisis of Precision".
 *
 * Never uses double/float for money. All arithmetic is done with BigDecimal
 * to guarantee absolute precision (no 0.1 + 0.2 = 0.30000000000000004 bugs).
 *
 * Every currency pair is routed through USD as the pivot:
 *   amountInUSD = amount / rate(FROM)     // rate(FROM) = units of FROM per 1 USD
 *   result      = amountInUSD * rate(TO)
 *
 * This means a direct USD conversion (FROM=USD or TO=USD) and a cross-rate
 * conversion (e.g. EUR -> INR) use exactly the same formula - no special
 * casing required.
 */
public class ConversionEngine {

    /** High intermediate precision so the pivot division never loses accuracy. */
    private static final MathContext INTERMEDIATE_PRECISION = new MathContext(34, RoundingMode.HALF_EVEN);

    /** Final financial output is always rounded to exactly 2 decimal places, Banker's rounding. */
    private static final int FINAL_SCALE = 2;
    private static final RoundingMode FINAL_ROUNDING = RoundingMode.HALF_EVEN;

    private final ExchangeRateProvider provider;

    public ConversionEngine(ExchangeRateProvider provider) {
        this.provider = provider;
    }

    /**
     * Converts {@code amount} from {@code fromCurrency} to {@code toCurrency},
     * routing through the USD pivot, and rounds the result using Banker's
     * rounding (HALF_EVEN) to 2 decimal places.
     */
    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) throws Exception {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must not be negative.");
        }

        BigDecimal fromRate = provider.getRateToUSD(fromCurrency); // units of FROM per 1 USD
        BigDecimal toRate = provider.getRateToUSD(toCurrency);     // units of TO per 1 USD

        BigDecimal amountInUsd = amount.divide(fromRate, INTERMEDIATE_PRECISION);
        BigDecimal result = amountInUsd.multiply(toRate, INTERMEDIATE_PRECISION);

        return result.setScale(FINAL_SCALE, FINAL_ROUNDING);
    }
}
