package com.decodelabs.currencyconverter;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Set;

/**
 * "Synthesis: The Complete Financial Engine" + "Continuous Routing: The Switch Board".
 *
 * Intake  (Scanner + do-while/switch + Security Gate)
 *   -> Combustion (BigDecimal cross-rate ConversionEngine)
 *   -> Exhaust (CurrencyFormatter)
 *   -> optional Turbocharger (LiveExchangeRateProvider via HttpURLConnection + GSON)
 */
public class Main {

    // Replace with your free key from https://www.exchangerate-api.com/ to enable live mode.
    private static final String LIVE_API_KEY = "YOUR_API_KEY_HERE";

    private static final Scanner scanner = new Scanner(System.in);
    private static final InputValidator validator = new InputValidator(scanner);
    private static final ExchangeRateProvider staticProvider = new StaticExchangeRateProvider();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   CURRENCY CONVERTER - DecodeLabs Project 4");
        System.out.println("=========================================");

        boolean running = true;
        do {
            printMenu();
            String choice = validator.readMenuChoice("Enter choice: ");

            switch (choice) {
                case "1":
                    runConversion(staticProvider);
                    break;
                case "2":
                    runConversion(buildLiveProvider());
                    break;
                case "3":
                    showSupportedCurrencies(staticProvider);
                    break;
                case "0":
                    running = false;
                    System.out.println("Goodbye! Engine shut down safely.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid menu option.");
            }
            System.out.println();
        } while (running);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("----------------- MENU -----------------");
        System.out.println("1. Convert Currency (Offline / Static Rates)");
        System.out.println("2. Convert Currency (Live Rates via API)");
        System.out.println("3. View Supported Currencies");
        System.out.println("0. Exit");
    }

    /** The Intake -> Combustion -> Exhaust pipeline for a single conversion. */
    private static void runConversion(ExchangeRateProvider provider) {
        if (provider == null) {
            return; // live provider could not be built (e.g. no API key set)
        }

        Set<String> supported = provider.supportedCurrencies();
        if (supported.isEmpty()) {
            System.out.println("No currencies available from \"" + provider.getName() + "\". Try another option.");
            return;
        }

        System.out.println("Using: " + provider.getName());
        String from = validator.readSupportedCurrency("From Currency: ", supported);
        String to = validator.readSupportedCurrency("To Currency: ", supported);
        BigDecimal amount = validator.readPositiveAmount("Amount: ");

        try {
            ConversionEngine engine = new ConversionEngine(provider);
            BigDecimal result = engine.convert(amount, from, to);

            System.out.println("-----------------------------------------");
            System.out.println("Input           : " + CurrencyFormatter.format(amount, from));
            System.out.println("Converted Amount: " + CurrencyFormatter.format(result, to));
            System.out.println("-----------------------------------------");
        } catch (Exception e) {
            System.out.println("Conversion failed: " + e.getMessage());
        }
    }

    private static void showSupportedCurrencies(ExchangeRateProvider provider) {
        System.out.println("Supported currencies (" + provider.getName() + "): " + provider.supportedCurrencies());
    }

    /** Builds the Turbocharger provider, or null (with a message) if no key is configured. */
    private static ExchangeRateProvider buildLiveProvider() {
        if (LIVE_API_KEY == null || LIVE_API_KEY.isBlank() || LIVE_API_KEY.equals("YOUR_API_KEY_HERE")) {
            System.out.println("Live mode is not configured. Set LIVE_API_KEY in Main.java");
            System.out.println("(get a free key at https://www.exchangerate-api.com/) to enable it.");
            return null;
        }
        return new LiveExchangeRateProvider(LIVE_API_KEY);
    }
}
