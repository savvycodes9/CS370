package dao;

import java.io.*;
import java.nio.file.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import model.Notification;
import controller.NotificationController;



public class NotificationDAO {

    private static final String FILE_PATH = Paths.get(
            "Just_for_fun", "Database", "notifications.txt"
    ).toString();

    public NotificationDAO() {
        ensureFileExists();
    }

    // Ensure directory + file exist
    private void ensureFileExists() {
        try {
            Files.createDirectories(Paths.get("Just_for_fun", "Database"));
            Path p = Paths.get(FILE_PATH);
            if (!Files.exists(p)) Files.createFile(p);
        } catch (IOException ignored) {}
    }

    private int getNextId(List<Notification> list) {
        int max = 0;
        for (Notification n : list)
            if (n.getNotificationId() > max) max = n.getNotificationId();
        return max + 1;
    }

    // Load ALL notifications
    public List<Notification> getAllNotifications() {
        ensureFileExists();
        List<Notification> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                // Format:
                // notificationId,userId,message,startTime,isRead
                if (p.length == 5) {
                    int id = Integer.parseInt(p[0]);
                    int userId = Integer.parseInt(p[1]);
                    String message = p[2];
                    LocalTime time = LocalTime.parse(p[3]);
                    boolean isRead = Boolean.parseBoolean(p[4]);

                    list.add(new Notification(id, userId, message, time, isRead));
                }
            }

        } catch (IOException ignored) {}

        return list;
    }

    public Notification getNotificationById(int id) {
        for (Notification n : getAllNotifications())
            if (n.getNotificationId() == id)
                return n;
        return null;
    }

    public List<Notification> getNotificationsByUser(int userId) {
        List<Notification> matches = new ArrayList<>();
        for (Notification n : getAllNotifications())
            if (n.getUserId() == userId)
                matches.add(n);
        return matches;
    }

    public List<Notification> getUnreadNotificationsByUser(int userId) {
        List<Notification> unread = new ArrayList<>();
        for (Notification n : getNotificationsByUser(userId))
            if (!n.isRead())
                unread.add(n);
        return unread;
    }

    // Save new notification
    public boolean saveNotification(Notification n) {
        List<Notification> list = getAllNotifications();
        int newId = getNextId(list);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            bw.write(newId + "," +
                    n.getUserId() + "," +
                    n.getMessage() + "," +
                    n.getStartTime() + "," +
                    n.isRead());

            bw.newLine();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    // Update existing notification
    public boolean updateNotification(Notification updated) {
        List<Notification> list = getAllNotifications();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Notification n : list) {
                if (n.getNotificationId() == updated.getNotificationId()) {

                    bw.write(updated.getNotificationId() + "," +
                            updated.getUserId() + "," +
                            updated.getMessage() + "," +
                            updated.getStartTime() + "," +
                            updated.isRead());

                } else {

                    bw.write(n.getNotificationId() + "," +
                            n.getUserId() + "," +
                            n.getMessage() + "," +
                            n.getStartTime() + "," +
                            n.isRead());
                }

                bw.newLine();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    // Delete notification
    public boolean deleteNotification(int id) {
        List<Notification> list = getAllNotifications();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Notification n : list) {
                if (n.getNotificationId() != id) {
                    bw.write(n.getNotificationId() + "," +
                            n.getUserId() + "," +
                            n.getMessage() + "," +
                            n.getStartTime() + "," +
                            n.isRead());
                    bw.newLine();
                }
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
