package dao;

import model.Availability;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityDAOImpl implements AvailabilityDAO {

    private static final String FILE_PATH = Paths.get(
            "Just_for_fun", "Database", "availability.txt"
    ).toString();

    public AvailabilityDAOImpl() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            Files.createDirectories(Paths.get("Just_for_fun", "Database"));
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);
        } catch (IOException ignored) {}
    }

    private int getNextId(List<Availability> list) {
        int max = 0;
        for (Availability a : list) {
            if (a.getAvailabilityId() > max) {
                max = a.getAvailabilityId();
            }
        }
        return max + 1;
    }

    @Override
    public List<Availability> getAllAvailabilities() {
        List<Availability> availabilities = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length == 6) {
                    int id = Integer.parseInt(p[0]);
                    int userId = Integer.parseInt(p[1]);
                    String date = p[2];
                    String start = p[3];
                    String end = p[4];
                    boolean available = Boolean.parseBoolean(p[5]);

                    availabilities.add(new Availability(id, userId, date, start, end, available));
                }
            }

        } catch (IOException ignored) {}

        return availabilities;
    }

    @Override
    public Availability getAvailabilityById(int availabilityId) {
        for (Availability a : getAllAvailabilities()) {
            if (a.getAvailabilityId() == availabilityId) {
                return a;
            }
        }
        return null;
    }

    @Override
    public List<Availability> getAvailabilitiesByUser(int userId) {
        List<Availability> result = new ArrayList<>();
        for (Availability a : getAllAvailabilities()) {
            if (a.getUserId() == userId) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public boolean saveAvailability(Availability availability) {

        List<Availability> list = getAllAvailabilities();
        int newId = getNextId(list);
        availability.setAvailabilityId(newId);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            bw.write(newId + "," +
                    availability.getUserId() + "," +
                    availability.getDate() + "," +
                    availability.getStartTime() + "," +
                    availability.getEndTime() + "," +
                    availability.isAvailable());
            bw.newLine();

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean updateAvailability(Availability updated) {

        List<Availability> list = getAllAvailabilities();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Availability a : list) {
                if (a.getAvailabilityId() == updated.getAvailabilityId()) {
                    a = updated; // replace
                }

                bw.write(a.getAvailabilityId() + "," +
                        a.getUserId() + "," +
                        a.getDate() + "," +
                        a.getStartTime() + "," +
                        a.getEndTime() + "," +
                        a.isAvailable());
                bw.newLine();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean deleteAvailability(int availabilityId) {

        List<Availability> list = getAllAvailabilities();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Availability a : list) {
                if (a.getAvailabilityId() != availabilityId) {
                    bw.write(a.getAvailabilityId() + "," +
                            a.getUserId() + "," +
                            a.getDate() + "," +
                            a.getStartTime() + "," +
                            a.getEndTime() + "," +
                            a.isAvailable());
                    bw.newLine();
                }
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
