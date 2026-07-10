import java.util.Scanner;

// ==========================================
// THE VAULT: Business Logic & Encapsulation
// ==========================================
class BankAccount {
    // Encapsulated state: immune to external interference
    private double balance;

    public BankAccount(double initialBalance) {
        if (initialBalance > 0) {
            this.balance = initialBalance;
        } else {
            this.balance = 0.0;
        }
    }

    // Getter for read-only access
    public double getBalance() {
        return this.balance;
    }

    // Mutator acting as a security checkpoint for withdrawals
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    // Mutator acting as a security checkpoint for deposits
    public boolean deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }
}

// ==========================================
// THE LOBBY: User Interface & Control Flow
// ==========================================
class ATM {
    private BankAccount account;
    private Scanner scanner;

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== DECODELABS ATM SYSTEM ===");
        System.out.println("Digital Twin Initialized. System Secure.");
        
        // Simulating the Idle -> Authenticated state transition
        System.out.print("\nPlease enter your PIN to access the terminal: ");
        getValidInt(); 

        boolean running = true;
        
        // IPO Loop (Input -> Process -> Output)
        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. Exit / Eject Card");
            System.out.print("Select an option (1-4): ");

            int choice = getValidInt();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    System.out.println("Transaction Complete. Ejecting card...");
                    running = false;
                    break;
                default:
                    System.out.println("ERROR: Invalid selection. Please choose 1-4.");
            }
        }
    }

    private void checkBalance() {
        System.out.printf("Current Balance: $%.2f\n", account.getBalance());
    }

    private void deposit() {
        System.out.print("Enter deposit amount: $");
        double amount = getValidDouble();
        
        if (account.deposit(amount)) {
            System.out.printf("SUCCESS: $%.2f deposited into your account.\n", amount);
        } else {
            System.out.println("ERROR: Invalid deposit amount. Must be greater than $0.");
        }
    }

    private void withdraw() {
        System.out.print("Enter withdrawal amount: $");
        double amount = getValidDouble();
        
        if (account.withdraw(amount)) {
            System.out.printf("SUCCESS: $%.2f withdrawn. Please take your cash.\n", amount);
        } else {
            System.out.println("ERROR: Insufficient funds or invalid amount.");
            System.out.printf("Current Balance: $%.2f\n", account.getBalance());
        }
    }

    // ==========================================
    // SECURITY GATE: Defensive Input Validation
    // ==========================================
    
    private double getValidDouble() {
        // Prevents InputMismatchException by checking tokens before consuming
        while (!scanner.hasNextDouble()) {
            System.out.print("ERROR: Invalid input. Please enter a valid numerical amount: $");
            scanner.next(); // Flush the bad token to prevent infinite loops
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Flush the leftover newline buffer
        return value;
    }

    private int getValidInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("ERROR: Invalid input. Please enter a valid number: ");
            scanner.next(); // Flush the bad token
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Flush the leftover newline buffer
        return value;
    }
}

// ==========================================
// MAIN EXECUTION
// ==========================================
public class DecodeLabs_Java_P3 {
    public static void main(String[] args) {
        // Instantiate the Data Vault with an initial balance
        BankAccount userAccount = new BankAccount(1000.00); 
        
        // Instantiate the ATM Interface and link it to the account
        ATM atm = new ATM(userAccount);
        
        // Power on the system
        atm.start();
    }
}