package dao;

import model.Event;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {

    private static final String FILE_PATH = Paths.get(
            "Just_for_fun", "Database", "events.txt"
    ).toString();

    public EventDAOImpl() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            Files.createDirectories(Paths.get("Just_for_fun", "Database"));
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);
        } catch (IOException ignored) {}
    }

    private int getNextId(List<Event> events) {
        int max = 0;
        for (Event e : events) {
            if (e.getEventId() > max) max = e.getEventId();
        }
        return max + 1;
    }

    // Read all events
    @Override
    public List<Event> getAllEvents() {
        ensureFileExists();
        List<Event> events = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 10) {
                    int id = Integer.parseInt(p[0]);
                    String title = p[1];
                    String description = p[2];
                    LocalDate date = LocalDate.parse(p[3]);
                    LocalTime start = LocalTime.parse(p[4]);
                    LocalTime end = LocalTime.parse(p[5]);
                    String location = p[6];

                    int groupId = Integer.parseInt(p[7]);
                    if (groupId < 0) groupId = -1; // null marker

                    int createdBy = Integer.parseInt(p[8]);
                    boolean isPrivate = Boolean.parseBoolean(p[9]);

                    events.add(new Event(
                            id, title, description, date, start, end,
                            location,
                            groupId == -1 ? null : groupId,
                            createdBy,
                            isPrivate
                    ));
                }
            }
        } catch (IOException ignored) {}

        return events;
    }

    @Override
    public List<Event> getEventsByDate(LocalDate date) {
        List<Event> all = getAllEvents();
        List<Event> result = new ArrayList<>();

        for (Event e : all) {
            if (e.getDate().equals(date)) {
                result.add(e);
            }
        }

        return result;
    }


    // Event lookups
    @Override
    public Event getEventById(int eventId) {
        for (Event e : getAllEvents()) {
            if (e.getEventId() == eventId) return e;
        }
        return null;
    }

    @Override
    public List<Event> getEventsByUser(int userId) {
        List<Event> list = new ArrayList<>();
        for (Event e : getAllEvents()) {
            if (e.getCreatedByUserId() == userId) list.add(e);
        }
        return list;
    }

    @Override
    public List<Event> getEventsByGroup(int groupId) {
        List<Event> list = new ArrayList<>();
        for (Event e : getAllEvents()) {
            if (e.getGroupId() != null && e.getGroupId() == groupId) list.add(e);
        }
        return list;
    }

    @Override
    public List<Event> getPublicEvents() {
        List<Event> list = new ArrayList<>();
        for (Event e : getAllEvents()) {
            if (!e.isPrivate()) list.add(e);
        }
        return list;
    }

    // Save event
    @Override
    public boolean saveEvent(Event event) {
        List<Event> events = getAllEvents();
        int newId = getNextId(events);
        event.setEventId(newId);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            bw.write(
                    newId + "," +
                            event.getTitle() + "," +
                            event.getDescription() + "," +
                            event.getDate() + "," +
                            event.getStartTime() + "," +
                            event.getEndTime() + "," +
                            event.getLocation() + "," +
                            (event.getGroupId() == null ? -1 : event.getGroupId()) + "," +
                            event.getCreatedByUserId() + "," +
                            event.isPrivate()
            );
            bw.newLine();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    // Update event
    @Override
    public boolean updateEvent(Event updatedEvent) {
        List<Event> events = getAllEvents();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Event e : events) {
                if (e.getEventId() == updatedEvent.getEventId()) {
                    // Write updated
                    bw.write(
                            updatedEvent.getEventId() + "," +
                                    updatedEvent.getTitle() + "," +
                                    updatedEvent.getDescription() + "," +
                                    updatedEvent.getDate() + "," +
                                    updatedEvent.getStartTime() + "," +
                                    updatedEvent.getEndTime() + "," +
                                    updatedEvent.getLocation() + "," +
                                    (updatedEvent.getGroupId() == null ? -1 : updatedEvent.getGroupId()) + "," +
                                    updatedEvent.getCreatedByUserId() + "," +
                                    updatedEvent.isPrivate()
                    );
                } else {
                    // Write original
                    bw.write(
                            e.getEventId() + "," +
                                    e.getTitle() + "," +
                                    e.getDescription() + "," +
                                    e.getDate() + "," +
                                    e.getStartTime() + "," +
                                    e.getEndTime() + "," +
                                    e.getLocation() + "," +
                                    (e.getGroupId() == null ? -1 : e.getGroupId()) + "," +
                                    e.getCreatedByUserId() + "," +
                                    e.isPrivate()
                    );
                }
                bw.newLine();
            }

            return true;

        } catch (IOException e1) {
            return false;
        }
    }

    // Delete event
    @Override
    public boolean deleteEvent(int eventId) {
        List<Event> events = getAllEvents();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Event e : events) {
                if (e.getEventId() != eventId) {
                    bw.write(
                            e.getEventId() + "," +
                                    e.getTitle() + "," +
                                    e.getDescription() + "," +
                                    e.getDate() + "," +
                                    e.getStartTime() + "," +
                                    e.getEndTime() + "," +
                                    e.getLocation() + "," +
                                    (e.getGroupId() == null ? -1 : e.getGroupId()) + "," +
                                    e.getCreatedByUserId() + "," +
                                    e.isPrivate()
                    );
                    bw.newLine();
                }
            }

            return true;

        } catch (IOException e1) {
            return false;
        }
    }
}
