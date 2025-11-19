package model;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private int scheduleId;
    private int userId;
    private List<Integer> availabilityIds;

    public Schedule(int scheduleId, int userId, List<Integer> availabilityIds) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.availabilityIds = availabilityIds != null ? availabilityIds : new ArrayList<>();
    }

    public int getScheduleId() { return scheduleId; }
    public int getUserId() { return userId; }
    public List<Integer> getAvailabilityIds() { return availabilityIds; }

    public void addAvailabilityId(int id) {
        availabilityIds.add(id);
    }
}
