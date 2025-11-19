package dao;

import model.User;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final String FILE_PATH =
            Paths.get("Just_for_fun", "Database", "users.txt").toString();

    public UserDAOImpl() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            // Create Database folder if missing
            Files.createDirectories(Paths.get("Just_for_fun", "Database"));

            // Create users.txt if missing
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path))
                Files.createFile(path);

        } catch (IOException ignored) {}
    }

    private int getNextId(List<User> users) {
        int max = 0;
        for (User u : users)
            if (u.getUserId() > max)
                max = u.getUserId();
        return max + 1;
    }

    @Override
    public List<User> getAllUsers() {
        ensureFileExists();
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");
                if (p.length == 5) {
                    int id = Integer.parseInt(p[0]);
                    String username = p[1];
                    String password = p[2];
                    String fullName = p[3];
                    String email = p[4];

                    users.add(new User(id, username, password, fullName, email));
                }
            }

        } catch (IOException ignored) {}

        return users;
    }

    @Override
    public User getUserById(int userId) {
        for (User u : getAllUsers()) {
            if (u.getUserId() == userId)
                return u;
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equalsIgnoreCase(username))
                return u;
        }
        return null;
    }


    @Override
    public boolean saveUser(User user) {
        List<User> users = getAllUsers();
        int newId = getNextId(users);

        // set new userId
        user.setUserId(newId);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            bw.write(newId + "," +
                    user.getUsername() + "," +
                    user.getPassword() + "," +
                    user.getFullName() + "," +
                    user.getEmail());
            bw.newLine();

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean updateUser(User updatedUser) {
        List<User> users = getAllUsers();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (User u : users) {

                if (u.getUserId() == updatedUser.getUserId()) {

                    bw.write(updatedUser.getUserId() + "," +
                            updatedUser.getUsername() + "," +
                            updatedUser.getPassword() + "," +
                            updatedUser.getFullName() + "," +
                            updatedUser.getEmail());

                } else {

                    bw.write(u.getUserId() + "," +
                            u.getUsername() + "," +
                            u.getPassword() + "," +
                            u.getFullName() + "," +
                            u.getEmail());
                }

                bw.newLine();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        List<User> users = getAllUsers();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (User u : users) {
                if (u.getUserId() != userId) {

                    bw.write(u.getUserId() + "," +
                            u.getUsername() + "," +
                            u.getPassword() + "," +
                            u.getFullName() + "," +
                            u.getEmail());
                    bw.newLine();
                }
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

}
