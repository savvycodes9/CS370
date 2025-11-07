import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    public static boolean saveEvent(String username, String eventName, String date, String description, boolean privateEvent) {
    Path eventsFile = Paths.get(System.getProperty("user.dir"))
                        .resolve("Just_for_fun")
                           .resolve("Database")
                           .resolve("events.txt")
                           .normalize();

    try (BufferedWriter bw = Files.newBufferedWriter(eventsFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
        bw.write(username + "," + eventName + "," + date + "," + description + "," + privateEvent);
        bw.newLine();
        System.out.println("Event saved to: " + eventsFile.toAbsolutePath());
        return true;
    } catch (IOException e) {
        System.out.println("Error saving event: " + e.getMessage());
        return false;
    }
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

        
        System.out.println("1. Login");
        System.out.print("event name: ");
        String name = sc.nextLine();
        System.out.print("date: ");
        String date = sc.nextLine();
        System.out.print("description: ");
        String description = sc.nextLine();
        System.out.print("Is this event private? Y/N: ");
        String ifPrivate = sc.nextLine().trim().toLowerCase();
        boolean privateEvent = false;
        privateEvent = ifPrivate.equals("y") || ifPrivate.equals("yes");
        if(saveEvent(username,name,date,description,privateEvent)) {
                System.out.println("Event saved!");
        } else {
                 System.out.println("Error saving event.");
        }
        

        //saveEvent("brand104", "meeting", "11/06/2025", "study meeting in room 101", false);
        

        sc.close();
     }

}
