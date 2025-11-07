import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private int scheduleId;
    private int userId;
    private List<Availability> timeSlots;

    public Schedule(int scheduleId, int userId, List<Availability> timeSlots) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.timeSlots = timeSlots != null ? timeSlots : new ArrayList<>();
    }

    public void addAvailability(Availability availability) {
        timeSlots.add(availability);
    }

    public void removeAvailability(int availabilityId) {
        timeSlots.removeIf(a -> a.getAvailabilityId() == availabilityId);
    }

    // Only return blocks marked available
    public List<Availability> getFreeSlots() {
        List<Availability> free = new ArrayList<>();
        for (Availability a : timeSlots) {
            if (a.isAvailable()) {
                free.add(a);
            }
        }
        return free;
    }

    public int getScheduleId() { return scheduleId; }
    public int getUserId() { return userId; }
    public List<Availability> getTimeSlots() { return timeSlots; }
}
