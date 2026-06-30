package CORE_JAVA.mypackage;
import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

public class DecodeLabs_Java_P1 {
    public static void main(String[] args) {
        // Initialize the input stream capture and stochastic generation utilities
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        boolean playAgain = true;
        int totalScore = 0;

        System.out.println("=== DecodeLabs: The Number Game ===");
        System.out.println("Logic Engine Initialized.");

        // Session Persistence: do-while loop to allow multiple rounds
        do {
            // Generate a random number from 1 to 100 (handling the zero-index shift)
            int target = random.nextInt(100) + 1;
            int maxAttempts = 7; // Binary search efficiency limit
            int attempts = 0;
            boolean win = false;

            System.out.println("\nTarget acquired. I have generated a number between 1 and 100.");
            System.out.println("You have " + maxAttempts + " attempts to deduce it.");

            // Start Feedback Loop
            while (!win && attempts < maxAttempts) {
                System.out.print("Enter your guess: ");
                int guess = 0;

                // Defensive Engineering: Input Validation
                try {
                    guess = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Exception Caught: Invalid input. Please enter a whole number.");
                    scanner.nextLine(); // Flush the memory buffer to clear the invalid token
                    continue; // Restart the loop without counting as an attempt
                }

                attempts++;

                // Decision Logic
                if (guess == target) {
                    System.out.println("Correct! Target matched in " + attempts + " attempts.");
                    win = true;
                    // Calculate score based on remaining attempts
                    totalScore += (maxAttempts - attempts + 1) * 10; 
                } else if (guess > target) {
                    System.out.println("Too High");
                } else {
                    System.out.println("Too Low");
                }
            }

            // Termination State for loss
            if (!win) {
                System.out.println("Engine offline. You've exhausted all attempts! The target was " + target + ".");
            }

            System.out.println("Current Session Score: " + totalScore);

            // Play Again Logic & avoiding the 'Scanner Trap'
            System.out.print("\nPlay Again? [Y/N]: ");
            scanner.nextLine(); // Flush the buffer to consume the leftover '\n' from nextInt()
            String response = scanner.nextLine();
            
            if (!response.equalsIgnoreCase("Y")) {
                playAgain = false;
            }

        } while (playAgain);

        System.out.println("\nEngine shut down. Thank you for playing! Final Score: " + totalScore);
        scanner.close();
    }
}