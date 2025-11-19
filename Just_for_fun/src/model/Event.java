package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int eventId;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private Integer groupId;         // Nullable
    private int createdByUserId;
    private boolean isPrivate;

    public Event(int eventId, String title, String description,
                 LocalDate date, LocalTime startTime, LocalTime endTime,
                 String location, Integer groupId,
                 int createdByUserId, boolean isPrivate) {

        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.groupId = groupId;
        this.createdByUserId = createdByUserId;
        this.isPrivate = isPrivate;
    }

    // Getters
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getLocation() { return location; }
    public Integer getGroupId() { return groupId; }
    public int getCreatedByUserId() { return createdByUserId; }
    public boolean isPrivate() { return isPrivate; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setLocation(String location) { this.location = location; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    public void setEventId(int eventId) { this.eventId = eventId; }

}
