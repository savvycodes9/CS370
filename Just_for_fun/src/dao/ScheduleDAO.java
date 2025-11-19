package dao;

import model.Schedule;
import java.util.List;

public interface ScheduleDAO {

    Schedule getScheduleById(int scheduleId);

    Schedule getScheduleByUserId(int userId);

    List<Schedule> getAllSchedules();

    boolean saveSchedule(Schedule schedule);

    boolean createScheduleForUser(int userId);

    boolean updateSchedule(Schedule schedule);

    boolean deleteSchedule(int scheduleId);
}
