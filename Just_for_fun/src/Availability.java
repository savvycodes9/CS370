public class Availability {
    private int availabilityId;
    private int userId;
    private String date;       // "2025-01-27"
    private String startTime;  // "14:00"
    private String endTime;    // "16:00"
    private boolean isAvailable;

    public Availability(int availabilityId, int userId,
                        String date, String startTime, String endTime,
                        boolean isAvailable) {

        this.availabilityId = availabilityId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }

    public int getAvailabilityId() { return availabilityId; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { this.isAvailable = available; }
}
