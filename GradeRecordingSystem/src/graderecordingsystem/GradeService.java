/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graderecordingsystem;

/**
 *
 * @author yanjul
 */
import java.io.*;
import java.util.*;

public class GradeService {
    private ArrayList<StudentRecord> students = new ArrayList<>();
    private static final String SAVEFILE = "grades.txt";

    public void addStudent(StudentRecord s) { students.add(s); }
    public ArrayList<StudentRecord> getAll() { return students; }

    public StudentRecord findById(String id) {
        for (StudentRecord s : students)
            if (s.getId().equalsIgnoreCase(id)) return s;
        return null;
    }

    public boolean deleteById(String id) {
        StudentRecord s = findById(id);
        if (s != null) { students.remove(s); return true; }
        return false;
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SAVEFILE))) {
            for (StudentRecord s : students) {
                bw.write(s.serialize()); bw.newLine();
            }
            System.out.println("Data saved.");
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void loadFromFile() {
        students.clear();
        File f = new File(SAVEFILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                StudentRecord s = StudentRecord.deserialize(line);
                if (s != null) students.add(s);
            }
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }
        public String getHighestGrade() {
        double max = -1;
        String info = "";
        for (StudentRecord s : students) {
            for (Map.Entry<String, Double> e : s.getSubjectGrades().entrySet()) {
                if (e.getValue() > max) {
                    max = e.getValue();
                    info = "Student: " + s.getName() + " | Subject: " + e.getKey() + " | Grade: " + e.getValue();
                }
            }
        }
        return max >= 0 ? info : "No grades available.";
    }

    public String getLowestGrade() {
        double min = 101;
        String info = "";
        for (StudentRecord s : students) {
            for (Map.Entry<String, Double> e : s.getSubjectGrades().entrySet()) {
                if (e.getValue() < min) {
                    min = e.getValue();
                    info = "Student: " + s.getName() + " | Subject: " + e.getKey() + " | Grade: " + e.getValue();
                }
            }
        }
        return min <= 100 ? info : "No grades available.";
    }

    public void printStudentAverages() {
        for (StudentRecord s : students) {
            System.out.printf("Student: %s | Average: %.2f%n", s.getName(), s.computeAverage());
        }
    }

    public void printSubjectAverages() {
        Map<String, Double> subjectSum = new HashMap<>();
        Map<String, Integer> subjectCount = new HashMap<>();

        for (StudentRecord s : students) {
            for (Map.Entry<String, Double> e : s.getSubjectGrades().entrySet()) {
                subjectSum.put(e.getKey(), subjectSum.getOrDefault(e.getKey(), 0.0) + e.getValue());
                subjectCount.put(e.getKey(), subjectCount.getOrDefault(e.getKey(), 0) + 1);
            }
        }

        if (subjectSum.isEmpty()) {
            System.out.println("No subjects available.");
            return;
        }

        for (String subj : subjectSum.keySet()) {
            double avg = subjectSum.get(subj) / subjectCount.get(subj);
            System.out.printf("Subject: %s | Average: %.2f%n", subj, avg);
        }
    }
}
