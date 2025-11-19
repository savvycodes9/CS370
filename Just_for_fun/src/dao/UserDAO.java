package dao;

import model.User;
import java.util.List;

public interface UserDAO {

    List<User> getAllUsers();

    User getUserById(int userId);

    User getUserByUsername(String username);

    boolean saveUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(int userId);
}
