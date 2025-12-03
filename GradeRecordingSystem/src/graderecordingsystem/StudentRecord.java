/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graderecordingsystem;

/**
 *
 * @author yanjul
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class StudentRecord {
    private String id;
    private String name;
    private LinkedHashMap<String, Double> subjectGrades;

    public StudentRecord(String id, String name) {
        this.id = id;
        this.name = name;
        this.subjectGrades = new LinkedHashMap<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Map<String, Double> getSubjectGrades() { return subjectGrades; }

    public void setGrade(String subject, double grade) { subjectGrades.put(subject.trim(), grade); }
    public Double getGrade(String subject) { return subjectGrades.get(subject); }

    public double computeAverage() {
        if (subjectGrades.isEmpty()) return 0.0;
        double sum = 0.0;
        for (double g : subjectGrades.values()) sum += g;
        return sum / subjectGrades.size();
    }

    public String serialize() {
        StringJoiner sj = new StringJoiner(",");
        for (Map.Entry<String, Double> e : subjectGrades.entrySet()) {
            sj.add(e.getKey().replace(",", " ") + ":" + e.getValue());
        }
        return id + "|" + name + "|" + sj.toString();
    }

    public static StudentRecord deserialize(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 2) return null;
            StudentRecord s = new StudentRecord(parts[0], parts[1]);
            if (parts.length >= 3 && !parts[2].trim().isEmpty()) {
                String[] pairs = parts[2].split(",");
                for (String p : pairs) {
                    if (p.trim().isEmpty()) continue;
                    String[] kv = p.split(":", 2);
                    if (kv.length == 2) s.setGrade(kv[0], Double.parseDouble(kv[1]));
                }
            }
            return s;
        } catch (Exception ex) { return null; }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append(" | Name: ").append(name).append("\n");
        if (subjectGrades.isEmpty()) sb.append("  No subjects/grades yet.\n");
        else {
            for (Map.Entry<String, Double> e : subjectGrades.entrySet())
                sb.append("  - ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            sb.append(String.format("  Average: %.2f\n", computeAverage()));
        }
        return sb.toString();
    }
}
