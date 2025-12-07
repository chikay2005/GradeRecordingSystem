/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package graderecordingsystem;

/**
 *
 * @author yanjul
 */
import java.io.Console;
import java.util.Scanner;

public class GradeSystem {
    private static Scanner sc = new Scanner(System.in);
    private static AuthService auth = new AuthService();
    private static GradeService gradeService = new GradeService();

    private static final String[] subjects = {"Math", "Science", "English", "History", "PE"};

    public static void main(String[] args) {
        System.out.println("=== Grade Recording System ===");

        gradeService.loadFromFile();

        while (true) {
            System.out.println("\n[1] Login");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");

            int choice = getValidInt("Choose: ", sc);
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> { 
                    System.out.println("Goodbye!"); 
                    System.exit(0); 
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        String password = readPassword("Password: ");

        if (!auth.login(username, password)) {
            System.out.println("Invalid credentials.");
            return;
        }

        System.out.println("Logged in as " + username);

        if (username.toLowerCase().startsWith("admin")) {
            new Admin(username, password, gradeService, sc).viewMenu();
        } else {
            new StudentUser(username, password, gradeService, sc).viewMenu();
        }
    }

    private static void register() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        String password = readPassword("Password: ");

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username/password cannot be empty.");
            return;
        }

        if (auth.register(username, password)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Username already exists.");
        }
    }

    public static String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            return new String(console.readPassword(prompt));
        } else {
            System.out.print(prompt + " (input hidden not supported in IDE): ");
            return sc.nextLine().trim();
        }
    }

    public static int getValidInt(String msg, Scanner sc) {
        while (true) {
            System.out.print(msg);
            String input = sc.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    // --- Admin helper methods ---
    public static void addStudent(GradeService service, Scanner sc) {
        System.out.print("Student ID: ");
        String id = sc.nextLine().trim();
        if (service.findById(id) != null) {
            System.out.println("Student ID already exists.");
            return;
        }
        System.out.print("Student Name: ");
        String name = sc.nextLine().trim();
        StudentRecord s = new StudentRecord(id, name);
        service.addStudent(s);
        System.out.println("Student added.");
    }

    public static void viewAllStudents(GradeService service) {
        if (service.getAll().isEmpty()) {
            System.out.println("No students yet.");
            return;
        }
        System.out.println("=== Students List ===");
        for (StudentRecord s : service.getAll())
            System.out.printf("ID: %s | Name: %s%n", s.getId(), s.getName());
    }

    public static void addOrUpdateGrade(GradeService service, Scanner sc) {
        if (service.getAll().isEmpty()) {
            System.out.println("No students available. Add students first.");
            return;
        }

        // Show students
        System.out.println("=== Students List ===");
        for (StudentRecord s : service.getAll())
            System.out.printf("ID: %s | Name: %s%n", s.getId(), s.getName());

        System.out.print("Enter Student ID to add/update grade: ");
        String id = sc.nextLine().trim();
        StudentRecord s = service.findById(id);
        if (s == null) { 
            System.out.println("Student not found."); 
            return; 
        }

        // Show subjects
        System.out.println("=== Subjects ===");
        for (int i = 0; i < subjects.length; i++)
            System.out.printf("[%d] %s%n", i + 1, subjects[i]);

        int subjChoice;
        while (true) {
            System.out.print("Choose subject (number): ");
            try {
                subjChoice = Integer.parseInt(sc.nextLine());
                if (subjChoice < 1 || subjChoice > subjects.length) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
            }
        }
        String subject = subjects[subjChoice - 1];

        double grade;
        while (true) {
            System.out.print("Grade (0-100): ");
            try {
                grade = Double.parseDouble(sc.nextLine());
                if (grade < 0 || grade > 100) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid grade. Must be 0-100.");
            }
        }

        s.setGrade(subject, grade);
        System.out.printf("Grade updated: %s - %.2f%n", subject, grade);
    }

    public static void deleteStudent(GradeService service, Scanner sc) {
        System.out.print("Student ID: ");
        String id = sc.nextLine().trim();
        if (service.deleteById(id)) System.out.println("Student deleted.");
        else System.out.println("Student not found.");
    }

    public static void showStatisticsMenu(GradeService service, Scanner sc) {
        if (service.getAll().isEmpty()) {
            System.out.println("No students to compute statistics.");
            return;
        }
        while (true) {
            System.out.println("\n=== Statistics Menu ===");
            System.out.println("[1] Highest grade");
            System.out.println("[2] Lowest grade");
            System.out.println("[3] Average per student");
            System.out.println("[4] Average per subject");
            System.out.println("[5] Back");

            int choice = getValidInt("Choose: ", sc);
            switch (choice) {
                case 1 -> System.out.println(service.getHighestGrade());
                case 2 -> System.out.println(service.getLowestGrade());
                case 3 -> service.printStudentAverages();
                case 4 -> service.printSubjectAverages();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
