package dao;

import model.Availability;
import java.util.List;

public interface AvailabilityDAO {

    List<Availability> getAllAvailabilities();

    Availability getAvailabilityById(int availabilityId);

    List<Availability> getAvailabilitiesByUser(int userId);

    boolean saveAvailability(Availability availability);

    boolean updateAvailability(Availability availability);

    boolean deleteAvailability(int availabilityId);
}
