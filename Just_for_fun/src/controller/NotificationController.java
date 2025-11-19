package controller;

import model.Notification;
import dao.NotificationDAO;

import java.time.LocalTime;
import java.util.List;

public class NotificationController {
    private NotificationDAO notificationDAO;
    
    public NotificationController() {
        this.notificationDAO = new NotificationDAO();
    }
    
    public boolean createNotification(int userId, String message, LocalTime startTime) {
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Notification message cannot be empty");
            return false;
        }
        
        Notification notification = new Notification(0, userId, message, startTime, false);
        return notificationDAO.saveNotification(notification);
    }
    
    public Notification getNotificationById(int notificationId) {
        return notificationDAO.getNotificationById(notificationId);
    }
    
    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }
    
    public List<Notification> getNotificationsByUser(int userId) {
        return notificationDAO.getNotificationsByUser(userId);
    }
    
    public List<Notification> getUnreadNotificationsByUser(int userId) {
        return notificationDAO.getUnreadNotificationsByUser(userId);
    }
    
    public boolean markNotificationAsRead(int notificationId) {
        Notification notification = notificationDAO.getNotificationById(notificationId);
        if (notification == null) {
            System.out.println("Notification not found");
            return false;
        }
        
        notification.markRead();
        return notificationDAO.updateNotification(notification);
    }
    
    public boolean markAllNotificationsAsRead(int userId) {
        List<Notification> notifications = notificationDAO.getNotificationsByUser(userId);
        boolean success = true;
        
        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                notification.markRead();
                if (!notificationDAO.updateNotification(notification)) {
                    success = false;
                }
            }
        }
        
        return success;
    }
    
    public boolean deleteNotification(int notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }
    
    public int getUnreadNotificationCount(int userId) {
        return getUnreadNotificationsByUser(userId).size();
    }
}
