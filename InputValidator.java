package com.decodelabs.currencyconverter;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

/**
 * "The Security Gate: Defending the Pipeline" + "The Buffer Trap".
 *
 * Anticipates user error instead of trusting the input:
 *   - Rejects negative amounts with a clear message (never crashes, never
 *     produces nonsensical output).
 *   - Wraps Scanner reads in try/catch(InputMismatchException) and clears
 *     the scanner buffer with nextLine() in the catch block, so a bad
 *     token (e.g. the word "ten") never gets stuck and re-read forever.
 */
public class InputValidator {

    private final Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Repeatedly prompts until the user enters a valid, non-negative amount.
     * Survives both non-numeric input (InputMismatchException) and negative
     * numbers without crashing or corrupting the scanner buffer.
     */
    public BigDecimal readPositiveAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                BigDecimal amount = scanner.nextBigDecimal();
                scanner.nextLine(); // consume the trailing newline
                if (amount.signum() < 0) {
                    System.out.println("Amount cannot be negative. Please try again.");
                    continue;
                }
                return amount;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // THE FIX: clear the stuck token from the buffer
            }
        }
    }

    /**
     * Repeatedly prompts until the user enters a currency code present in
     * {@code supported}. Input is case-insensitive and trimmed.
     */
    public String readSupportedCurrency(String prompt, Set<String> supported) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (supported.contains(input)) {
                return input;
            }
            System.out.println("Unsupported currency \"" + input + "\". Supported: " + supported);
        }
    }

    /** Reads a raw menu-choice line (kept as String so switch-on-string works cleanly). */
    public String readMenuChoice(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
