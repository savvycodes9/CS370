package model;
import java.time.LocalTime;

public class Notification {
    private int notificationId;
    private int userId;
    private String message;
    private LocalTime startTime;
    private boolean isRead;

    public Notification(int notificationId, int userId, String message,
                        LocalTime startTime, boolean isRead) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.startTime = startTime;
        this.isRead = isRead;
    }

    public int getNotificationId() { return notificationId; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public LocalTime getStartTime() { return startTime; }
    public boolean isRead() { return isRead; }

    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public void markRead() {
        this.isRead = true;
    }
}
