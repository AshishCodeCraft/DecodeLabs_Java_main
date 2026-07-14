package com.decodelabs.currencyconverter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * "Static Hardcoded Values" mode from the Turbocharger slide. Works fully
 * offline, no internet or API key required. Rates are units-of-currency
 * per 1 USD (USD is the pivot).
 */
public class StaticExchangeRateProvider implements ExchangeRateProvider {

    private final Map<String, BigDecimal> ratesToUsd = new LinkedHashMap<>();

    public StaticExchangeRateProvider() {
        // Approximate reference rates: 1 USD = X units of currency
        ratesToUsd.put("USD", new BigDecimal("1"));
        ratesToUsd.put("EUR", new BigDecimal("0.9200"));
        ratesToUsd.put("GBP", new BigDecimal("0.7900"));
        ratesToUsd.put("INR", new BigDecimal("83.5000"));
        ratesToUsd.put("JPY", new BigDecimal("151.2000"));
        ratesToUsd.put("AUD", new BigDecimal("1.5200"));
        ratesToUsd.put("CAD", new BigDecimal("1.3700"));
        ratesToUsd.put("KWD", new BigDecimal("0.3070"));
    }

    @Override
    public BigDecimal getRateToUSD(String currencyCode) throws Exception {
        BigDecimal rate = ratesToUsd.get(currencyCode.toUpperCase());
        if (rate == null) {
            throw new IllegalArgumentException("Unsupported currency: " + currencyCode);
        }
        return rate;
    }

    @Override
    public Set<String> supportedCurrencies() {
        return Collections.unmodifiableSet(ratesToUsd.keySet());
    }

    @Override
    public String getName() {
        return "Static (Offline) Rates";
    }
}
