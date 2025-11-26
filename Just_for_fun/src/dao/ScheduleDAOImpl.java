package dao;

import model.Schedule;
import model.Availability;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ScheduleDAOImpl implements ScheduleDAO {

    private static final String FILE_PATH = Paths.get(
            "Just_for_fun", "Database", "schedules.txt"
    ).toString();

    public ScheduleDAOImpl() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            Files.createDirectories(Paths.get("Just_for_fun", "Database"));
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);
        } catch (IOException ignored) {}
    }

    // Read all schedules from file
    @Override
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length == 3) {
                    int scheduleId = Integer.parseInt(p[0]);
                    int userId = Integer.parseInt(p[1]);

                    List<Integer> availabilityIds = new ArrayList<>();
                    if (!p[2].equals("null") && !p[2].isBlank()) {
                        for (String id : p[2].split("\\|")) {
                            availabilityIds.add(Integer.parseInt(id.trim()));
                        }
                    }

                    schedules.add(new Schedule(scheduleId, userId, availabilityIds));
                }
            }

        } catch (IOException ignored) {}

        return schedules;
    }

    // Get schedule by ID
    @Override
    public Schedule getScheduleById(int scheduleId) {
        for (Schedule s : getAllSchedules()) {
            if (s.getScheduleId() == scheduleId) return s;
        }
        return null;
    }

    // Get schedule by userId
    @Override
    public Schedule getScheduleByUserId(int userId) {
        for (Schedule s : getAllSchedules()) {
            if (s.getUserId() == userId) return s;
        }
        return null;
    }

    // Create a blank schedule for a new user
    @Override
    public boolean createScheduleForUser(int userId) {
        int newId = getNextId();

        Schedule schedule = new Schedule(newId, userId, new ArrayList<>());

        return saveSchedule(schedule);
    }

    // Save (append) schedule to file
    @Override
    public boolean saveSchedule(Schedule schedule) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            String availabilityString =
                    schedule.getAvailabilityIds().isEmpty()
                            ? "null"
                            : String.join("|",
                            schedule.getAvailabilityIds().stream()
                                    .map(String::valueOf)
                                    .toList()
                    );

            bw.write(schedule.getScheduleId() + "," +
                    schedule.getUserId() + "," +
                    availabilityString);

            bw.newLine();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    // Update schedule (rewrite entire file)
    @Override
    public boolean updateSchedule(Schedule updated) {
        List<Schedule> all = getAllSchedules();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Schedule s : all) {
                if (s.getScheduleId() == updated.getScheduleId()) {
                    s = updated;
                }

                String availabilityString =
                        s.getAvailabilityIds().isEmpty()
                                ? "null"
                                : String.join("|",
                                s.getAvailabilityIds().stream()
                                        .map(String::valueOf)
                                        .toList()
                        );

                bw.write(s.getScheduleId() + "," +
                        s.getUserId() + "," +
                        availabilityString);

                bw.newLine();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    // Delete schedule
    @Override
    public boolean deleteSchedule(int scheduleId) {
        List<Schedule> all = getAllSchedules();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Schedule s : all) {
                if (s.getScheduleId() != scheduleId) {

                    String availabilityString =
                            s.getAvailabilityIds().isEmpty()
                                    ? "null"
                                    : String.join("|",
                                    s.getAvailabilityIds().stream()
                                            .map(String::valueOf)
                                            .toList()
                            );

                    bw.write(s.getScheduleId() + "," +
                            s.getUserId() + "," +
                            availabilityString);
                    bw.newLine();
                }
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    // Get next auto-incremented ID
    private int getNextId() {
        int max = 0;
        for (Schedule s : getAllSchedules()) {
            if (s.getScheduleId() > max) max = s.getScheduleId();
        }
        return max + 1;
    }
}
