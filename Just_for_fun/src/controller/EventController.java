package controller;

import dao.EventDAO;
import dao.EventDAOImpl;
import model.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventController {

    private EventDAO eventDAO;

    public EventController() {
        this.eventDAO = new EventDAOImpl();
    }

    // Create Event
    public boolean createEvent(String title, String description,
                               LocalDate date, LocalTime startTime,
                               LocalTime endTime, String location,
                               Integer groupId, int createdByUserId,
                               boolean isPrivate) {

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Event title cannot be empty");
            return false;
        }

        if (date == null) {
            System.out.println("Event date cannot be null");
            return false;
        }

        if (startTime == null || endTime == null) {
            System.out.println("Event times cannot be null");
            return false;
        }

        if (endTime.isBefore(startTime)) {
            System.out.println("End time cannot be before start time");
            return false;
        }

        Event newEvent = new Event(
                0, title, description, date, startTime, endTime,
                location, groupId, createdByUserId, isPrivate
        );

        return eventDAO.saveEvent(newEvent);
    }

    // Get event by id
    public Event getEventById(int eventId) {
        return eventDAO.getEventById(eventId);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    // Get user events
    public List<Event> getEventsByUser(int userId) {
        return eventDAO.getEventsByUser(userId);
    }

    // Get group events
    public List<Event> getEventsByGroup(int groupId) {
        return eventDAO.getEventsByGroup(groupId);
    }

    // Get public events
    public List<Event> getPublicEvents() {
        return eventDAO.getPublicEvents();
    }

    // Update event
    public boolean updateEvent(int eventId, String title, String description,
                               LocalDate date, LocalTime startTime,
                               LocalTime endTime, String location,
                               boolean isPrivate) {

        Event event = eventDAO.getEventById(eventId);
        if (event == null) {
            System.out.println("Event not found");
            return false;
        }

        if (title != null && !title.trim().isEmpty())
            event.setTitle(title);

        if (description != null)
            event.setDescription(description);

        if (date != null)
            event.setDate(date);

        if (startTime != null)
            event.setStartTime(startTime);

        if (endTime != null)
            event.setEndTime(endTime);

        if (location != null)
            event.setLocation(location);

        event.setPrivate(isPrivate);

        return eventDAO.updateEvent(event);
    }

    // Delete event
    public boolean deleteEvent(int eventId) {
        return eventDAO.deleteEvent(eventId);
    }

    // Authorization check
    public boolean isUserAuthorizedToModifyEvent(int userId, int eventId) {
        Event event = eventDAO.getEventById(eventId);
        return event != null && event.getCreatedByUserId() == userId;
    }
}
