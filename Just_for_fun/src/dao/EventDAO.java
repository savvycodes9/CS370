package dao;

import model.Event;
import java.time.LocalDate;
import java.util.List;

public interface EventDAO {

    List<Event> getAllEvents();

    Event getEventById(int eventId);

    List<Event> getEventsByUser(int userId);

    List<Event> getEventsByGroup(int groupId);

    List<Event> getEventsByDate(LocalDate date);

    List<Event> getPublicEvents();

    boolean saveEvent(Event event);

    boolean updateEvent(Event event);

    boolean deleteEvent(int eventId);
}
