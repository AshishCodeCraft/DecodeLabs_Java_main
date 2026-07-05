import java.util.Scanner;

public class DecodeLabs_Java_P2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== DecodeLabs: Student Grade Calculator ===");
        System.out.println("Logic Processing Unit Initialized.\n");
        
        int numSubjects = 0;
        
        while (true) {
            System.out.print("Enter the number of subjects: ");
            try {
                numSubjects = Integer.parseInt(scanner.nextLine()); 
                if (numSubjects > 0) {
                    break;
                }
                System.out.println("Error: Number of subjects must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Exception: Invalid input. Please enter a whole number.");
            }
        }
        
        int totalMarks = 0;
        
        for (int i = 1; i <= numSubjects; i++) {
            int currentMark = 0;
            
            while (true) {
                System.out.print("Enter marks obtained in Subject " + i + " (0-100): ");
                try {
                    currentMark = Integer.parseInt(scanner.nextLine());
                    
                    if (currentMark >= 0 && currentMark <= 100) {
                        break; 
                    } else {
                        System.out.println("Error: Marks must be between 0 and 100. Value rejected.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Exception: Invalid input. Please enter a whole number.");
                }
            }
            totalMarks += currentMark;
        }
        
        double average = (double) totalMarks / numSubjects;
        
        String grade;
        if (average >= 90) {
            grade = "A";
        } else if (average >= 80) {
            grade = "B";
        } else if (average >= 70) {
            grade = "C";
        } else if (average >= 60) {
            grade = "D";
        } else {
            grade = "F"; 
        }
        
        System.out.println("\n========================================");
        System.out.println("          ACADEMIC REPORT               ");
        System.out.println("========================================");
        System.out.println("Total Subjects: " + numSubjects);
        System.out.println("Total Marks   : " + totalMarks + " / " + (numSubjects * 100));
        System.out.printf("Average       : %.2f%%\n", average); 
        System.out.println("Final Grade   : " + grade);
        System.out.println("========================================");
        
        scanner.close();
    }
}