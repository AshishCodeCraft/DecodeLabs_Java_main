package com.decodelabs.currencyconverter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * "The Turbocharger: Enterprise Scale" — fetches live conversion rates from
 * ExchangeRate-API (https://www.exchangerate-api.com) instead of relying on
 * static hardcoded values.
 *
 * Pipeline (matches "The API Connection Pipeline" slide):
 *   1. URL Construction   - inject the API key into the endpoint string
 *   2. Connection Mgmt    - open HttpURLConnection, method GET
 *   3. Response Verify    - ensure HTTP 200 OK before proceeding
 *   4. Data Extraction    - read the raw JSON via InputStream/BufferedReader
 *
 * Then "JSON X-Ray: Parsing with GSON" — drill into conversion_rates -> the
 * target currency key -> convert it to a BigDecimal for the engine.
 *
 * NOTE: You need a free API key from https://www.exchangerate-api.com/
 * and the GSON library (com.google.code.gson:gson) on your classpath.
 * See README.md for setup instructions.
 */
public class LiveExchangeRateProvider implements ExchangeRateProvider {

    private static final String ENDPOINT_TEMPLATE =
            "https://v6.exchangerate-api.com/v6/%s/latest/USD";

    private final String apiKey;
    private JsonObject cachedRates; // conversion_rates object, cached after first successful fetch

    public LiveExchangeRateProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public BigDecimal getRateToUSD(String currencyCode) throws Exception {
        JsonObject rates = fetchConversionRates();
        String code = currencyCode.toUpperCase();
        if (!rates.has(code)) {
            throw new IllegalArgumentException("Currency not found in live rates: " + code);
        }
        return rates.get(code).getAsBigDecimal();
    }

    @Override
    public Set<String> supportedCurrencies() {
        try {
            JsonObject rates = fetchConversionRates();
            Set<String> codes = new HashSet<>();
            for (String key : rates.keySet()) {
                codes.add(key);
            }
            return codes;
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    @Override
    public String getName() {
        return "Live Rates (ExchangeRate-API)";
    }

    /** Step 1-4 of the API Connection Pipeline, executed once per session (cached). */
    private JsonObject fetchConversionRates() throws Exception {
        if (cachedRates != null) {
            return cachedRates;
        }

        // 1. URL Construction
        String url = String.format(ENDPOINT_TEMPLATE, apiKey);

        // 2. Connection Management
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // 3. Response Verification
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API request failed with HTTP status: " + status);
        }

        // 4. Data Extraction
        StringBuilder rawJson = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rawJson.append(line);
            }
        } finally {
            connection.disconnect();
        }

        // JSON X-Ray: Parsing with GSON
        JsonObject root = JsonParser.parseString(rawJson.toString()).getAsJsonObject();
        String result = root.has("result") ? root.get("result").getAsString() : "";
        if (!"success".equalsIgnoreCase(result)) {
            throw new RuntimeException("API returned an error result: " + rawJson);
        }

        cachedRates = root.getAsJsonObject("conversion_rates");
        return cachedRates;
    }
}
