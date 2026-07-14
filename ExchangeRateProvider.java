package com.decodelabs.currencyconverter;

import java.math.BigDecimal;
import java.util.Set;

/**
 * The "Turbocharger" contract. Any source of exchange rates (static table or
 * live API) must be able to report a currency's rate relative to the pivot
 * currency (USD) and the set of currencies it knows about.
 *
 * All rates are expressed as "units of currency X per 1 USD", matching the
 * ExchangeRate-API convention: { "base_code": "USD", "conversion_rates": { "INR": 83.41, ... } }
 */
public interface ExchangeRateProvider {

    /**
     * @param currencyCode ISO currency code, e.g. "INR"
     * @return units of currencyCode equal to 1 USD
     */
    BigDecimal getRateToUSD(String currencyCode) throws Exception;

    /** @return all currency codes this provider can supply rates for. */
    Set<String> supportedCurrencies();

    /** Human-readable name shown in the menu, e.g. "Static (Offline) Rates". */
    String getName();
}
