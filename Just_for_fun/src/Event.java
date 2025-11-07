import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int eventId;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private String location;
    private int groupId; // nullable for solo events
    private int createdByUserId;

    public Event(int eventId, String title, String description,
                 LocalDate date, LocalTime startTime,
                 String location, Integer groupId,
                 int createdByUserId) {

        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.location = location;
        this.groupId = groupId;
        this.createdByUserId = createdByUserId;
    }

    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public String getLocation() { return location; }
    public Integer getGroupId() { return groupId; }
    public int getCreatedByUserId() { return createdByUserId; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setLocation(String location) { this.location = location; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }
}
