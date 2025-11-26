package controller;

import dao.ScheduleDAO;
import dao.ScheduleDAOImpl;
import dao.AvailabilityDAO;
import dao.AvailabilityDAOImpl;

import model.Schedule;
import model.Availability;

import java.util.ArrayList;
import java.util.List;

public class ScheduleController {

    private ScheduleDAO scheduleDAO;
    private AvailabilityDAO availabilityDAO;

    public ScheduleController() {
        this.scheduleDAO = new ScheduleDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
    }

    // Get or create schedule
    public Schedule getOrCreateScheduleByUserId(int userId) {

        Schedule schedule = scheduleDAO.getScheduleByUserId(userId);

        if (schedule == null) {
            boolean created = scheduleDAO.createScheduleForUser(userId);
            if (!created) return null;

            schedule = scheduleDAO.getScheduleByUserId(userId);
        }

        return schedule;
    }

    // Direct schedule getters
    public Schedule getScheduleByUserId(int userId) {
        return scheduleDAO.getScheduleByUserId(userId);
    }

    public Schedule getScheduleById(int scheduleId) {
        return scheduleDAO.getScheduleById(scheduleId);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleDAO.getAllSchedules();
    }

    // Add availability block
    public boolean addAvailabilityToSchedule(int userId, String date, String startTime, String endTime, boolean isAvailable) {

        if (date == null || date.trim().isEmpty()) {
            System.out.println("Date cannot be empty");
            return false;
        }
        if (startTime == null || endTime == null) {
            System.out.println("Times cannot be null");
            return false;
        }
        if (startTime.compareTo(endTime) >= 0) {
            System.out.println("End time must be after start time");
            return false;
        }

        Schedule schedule = getOrCreateScheduleByUserId(userId);
        if (schedule == null) {
            System.out.println("Failed to get or create schedule");
            return false;
        }

        Availability a = new Availability(0, userId, date, startTime, endTime, isAvailable);

        boolean saved = availabilityDAO.saveAvailability(a);
        if (!saved) return false;

        // Add availability ID to schedule
        schedule.getAvailabilityIds().add(a.getAvailabilityId());
        return scheduleDAO.updateSchedule(schedule);
    }

    // Remove availability block
    public boolean removeAvailabilityFromSchedule(int userId, int availabilityId) {

        Schedule schedule = getScheduleByUserId(userId);
        if (schedule == null) {
            System.out.println("User schedule not found.");
            return false;
        }

        Availability availability = availabilityDAO.getAvailabilityById(availabilityId);
        if (availability == null || availability.getUserId() != userId) {
            System.out.println("Availability not found or does not belong to this user.");
            return false;
        }

        boolean removed = availabilityDAO.deleteAvailability(availabilityId);
        if (!removed) return false;

        schedule.getAvailabilityIds().remove(Integer.valueOf(availabilityId));
        return scheduleDAO.updateSchedule(schedule);
    }

    // Get user free time
    public List<Availability> getFreeSlots(int userId) {

        Schedule schedule = getScheduleByUserId(userId);
        if (schedule == null) return new ArrayList<>();

        List<Availability> free = new ArrayList<>();

        for (Integer id : schedule.getAvailabilityIds()) {
            Availability a = availabilityDAO.getAvailabilityById(id);
            if (a != null && a.isAvailable()) {
                free.add(a);
            }
        }

        return free;
    }

    // Get all availabiltiy for users
    public List<Availability> getAllAvailabilities(int userId) {

        Schedule schedule = getScheduleByUserId(userId);
        if (schedule == null) return new ArrayList<>();

        List<Availability> result = new ArrayList<>();

        for (Integer id : schedule.getAvailabilityIds()) {
            Availability a = availabilityDAO.getAvailabilityById(id);
            if (a != null) result.add(a);
        }

        return result;
    }

    // Update status
    public boolean updateAvailabilityStatus(int availabilityId, boolean isAvailable) {

        Availability availability = availabilityDAO.getAvailabilityById(availabilityId);
        if (availability == null) {
            System.out.println("Availability not found");
            return false;
        }

        availability.setAvailable(isAvailable);
        return availabilityDAO.updateAvailability(availability);
    }

    // Delete schedule
    public boolean deleteSchedule(int scheduleId) {
        return scheduleDAO.deleteSchedule(scheduleId);
    }
}
