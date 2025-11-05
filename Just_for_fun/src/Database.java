import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Database {
    //private static final String FILE_NAME = Paths.get(System.getProperty("user.dir"), "Database", "users.txt").toString();
    private static final String FILE_NAME = "Just_for_fun/Database/users.txt";

    // Load users into a map from file
    public static Map<String, String[]> loadUsers() {
        Map<String, String[]> users = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String username = parts[0];
                    String password = parts[1];
                    String fullname = parts[2];
                    String email = parts[3];
                    users.put(username, new String[]{password, fullname, email});
                }
            }
        } catch (IOException e) {
            System.out.println("No existing users found.");
            System.out.println(e);
        }
        return users;
    }
    
    //Checks if login is valid
     public static boolean login(String username, String password, Map<String, String[]> users) {
        if (users.containsKey(username)) {
            String[] userInfo = users.get(username);
            return userInfo[0].equals(password);
        }
        return false;
    }

     public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, String[]> users = loadUsers();

        System.out.println("1. Login");

        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (login(username, password, users)) {
                System.out.println("Login successful! Welcome " + users.get(username)[1]);
        } else {
                System.out.println("Invalid username or password.");
        }

        sc.close();
     }

}
