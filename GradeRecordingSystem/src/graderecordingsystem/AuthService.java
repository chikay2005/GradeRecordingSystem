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
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final String USERFILE = "users.txt";
    private Map<String, String> users = new HashMap<>();

    public AuthService() { loadUsers(); }

    private void loadUsers() {
        File f = new File(USERFILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) users.put(parts[0], parts[1]);
            }
        } catch (IOException e) { System.out.println("Error loading users: " + e.getMessage()); }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERFILE))) {
            for (Map.Entry<String, String> e : users.entrySet()) {
                bw.write(e.getKey() + "|" + e.getValue()); bw.newLine();
            }
        } catch (IOException e) { System.out.println("Error saving users: " + e.getMessage()); }
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, password); saveUsers(); return true;
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
