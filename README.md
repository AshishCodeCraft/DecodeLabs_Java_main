# Currency Converter — Java Programming Project 4 (DecodeLabs)

A console-based Java currency converter covering every requirement from the
"Industrial Training Kit — Project 4" spec: menu-driven CLI, input
validation ("Security Gate"), BigDecimal financial-grade precision,
cross-rate routing through a USD pivot, Banker's rounding, polished output
formatting, and an optional live-rate API integration.

## Project Structure

```
CurrencyConverter/
├── pom.xml                          # Maven build (needed for live/GSON mode)
├── src/main/java/com/decodelabs/currencyconverter/
│   ├── Main.java                    # CLI entry point (do-while + switch menu)
│   ├── ExchangeRateProvider.java    # Interface: any source of USD-pivot rates
│   ├── StaticExchangeRateProvider.java  # Hardcoded offline rates (8 currencies)
│   ├── LiveExchangeRateProvider.java    # HttpURLConnection + GSON, live rates
│   ├── ConversionEngine.java        # BigDecimal cross-rate math, HALF_EVEN rounding
│   ├── InputValidator.java          # Security Gate: negative & bad-input handling
│   └── CurrencyFormatter.java       # printf-style two-decimal financial output
└── README.md
```

## How each slide's requirement is implemented

| Requirement (from the deck)                     | Where |
|---------------------------------------------------|-------|
| Scanner-based intake of source/target/amount      | `InputValidator`, `Main` |
| `do-while` loop + `switch` menu, `break`/`default` | `Main.main()` |
| Security Gate — reject negative amounts            | `InputValidator.readPositiveAmount` |
| Buffer Trap fix — `InputMismatchException` + `nextLine()` clear | `InputValidator.readPositiveAmount` |
| Direct conversion & cross-rate routing via USD pivot | `ConversionEngine.convert` |
| `BigDecimal` instead of `double` (Crisis of Precision) | `ConversionEngine`, `StaticExchangeRateProvider` |
| `RoundingMode.HALF_EVEN` (Banker's rounding)       | `ConversionEngine.FINAL_ROUNDING` |
| Two-decimal, comma-grouped output (`%,10.2f`)      | `CurrencyFormatter.format` |
| Turbocharger — live API rates                      | `LiveExchangeRateProvider` |
| URL construction, GET, HTTP 200 check, stream read  | `LiveExchangeRateProvider.fetchConversionRates` |
| JSON parsing with GSON (`conversion_rates` → code)  | `LiveExchangeRateProvider.getRateToUSD` |
| Gatekeeper Checklist (multi-currency, input integrity, accuracy, formatting) | All of the above together |

## Build & Run — Offline mode (no dependencies needed)

The static/offline mode uses only the JDK, so you can compile it directly
with `javac` — no Maven or internet access required:

```bash
javac -d out src/main/java/com/decodelabs/currencyconverter/*.java
java -cp out com.decodelabs.currencyconverter.Main
```

> Note: `LiveExchangeRateProvider.java` imports GSON (`com.google.gson.*`).
> If GSON isn't on your classpath yet, use the Maven build below instead of
> plain `javac` — Maven will download GSON automatically.

## Build & Run — Live mode (real-time exchange rates)

1. Get a free API key from https://www.exchangerate-api.com/
2. Open `Main.java` and replace:
   ```java
   private static final String LIVE_API_KEY = "YOUR_API_KEY_HERE";
   ```
   with your real key.
3. Build with Maven (this pulls in GSON automatically):
   ```bash
   mvn clean package
   java -jar target/currency-converter.jar
   ```
4. Choose menu option **2** to convert using live, real-time rates.

If you don't configure a key, option 2 simply prints a friendly message and
returns to the menu instead of crashing — the app never assumes live mode
is available.

## Example Session

```
----------------- MENU -----------------
1. Convert Currency (Offline / Static Rates)
2. Convert Currency (Live Rates via API)
3. View Supported Currencies
0. Exit
Enter choice: 1
Using: Static (Offline) Rates
From Currency: USD
To Currency: INR
Amount: 100
-----------------------------------------
Input           :     100.00 USD
Converted Amount:   8,350.00 INR
-----------------------------------------
```

Bad input is handled gracefully instead of crashing:

```
Amount: ten
Please enter a valid number.
Amount: -5
Amount cannot be negative. Please try again.
Amount: 25
```

## Supported Currencies (offline mode)

`USD, EUR, GBP, INR, JPY, AUD, CAD, KWD` — edit the map in
`StaticExchangeRateProvider` to add more.

## Extending Further

- Add JUnit tests for `ConversionEngine` (edge cases: zero amount, unknown
  currency, extreme precision)
- Add a `ConversionHistory` feature that logs each conversion to a file
- Swap the console UI for a small Swing/JavaFX front-end (the slides even
  mock up a "Currency Converter" desktop window)
