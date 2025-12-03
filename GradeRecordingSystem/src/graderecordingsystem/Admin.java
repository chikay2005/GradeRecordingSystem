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

public class Admin extends User {
    private GradeService gradeService;
    private Scanner sc;

    public Admin(String username, String password, GradeService gradeService, Scanner sc) {
        super(username, password);
        this.gradeService = gradeService;
        this.sc = sc;
    }

    @Override
    public void viewMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("[1] Add student");
            System.out.println("[2] View all students");
            System.out.println("[3] Add/Update grade");
            System.out.println("[4] Delete student");
            System.out.println("[5] Compute statistics");
            System.out.println("[6] Save to file");
            System.out.println("[7] Load from file");
            System.out.println("[8] Logout");

            int choice = GradeSystem.getValidInt("Choose option: ", sc);

            switch (choice) {
                case 1 -> GradeSystem.addStudent(gradeService, sc);
                case 2 -> GradeSystem.viewAllStudents(gradeService);
                case 3 -> GradeSystem.addOrUpdateGrade(gradeService, sc);
                case 4 -> GradeSystem.deleteStudent(gradeService, sc);
                case 5 -> GradeSystem.showStatisticsMenu(gradeService, sc);
                case 6 -> gradeService.saveToFile();
                case 7 -> { gradeService.loadFromFile(); System.out.println("✅ Data loaded."); }
                case 8 -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
}
