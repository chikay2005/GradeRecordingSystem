/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graderecordingsystem;

/**
 *
 * @author yanjul
 */
import java.util.Scanner;

public class StudentUser extends User {
    private GradeService gradeService;
    private Scanner sc;

    public StudentUser(String username, String password, GradeService gradeService, Scanner sc) {
        super(username, password);
        this.gradeService = gradeService;
        this.sc = sc;
    }

    @Override
    public void viewMenu() {
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("[1] View my grades");
            System.out.println("[2] Compute my average");
            System.out.println("[3] Logout");

            int choice = GradeSystem.getValidInt("Choose option: ", sc);
            switch (choice) {
                case 1 -> {
                    StudentRecord s = gradeService.findById(username);
                    if (s != null) {
                        System.out.println(s);
                    } else {
                        System.out.println("No record found.");
                    }
                }
                case 2 -> {
                    StudentRecord s = gradeService.findById(username);
                    if (s != null) System.out.printf("Average: %.2f%n", s.computeAverage());
                    else System.out.println("No record found.");
                }
                case 3 -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
