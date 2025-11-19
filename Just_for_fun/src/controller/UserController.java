package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;

public class UserController {

    private UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAOImpl();
    }

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);

        if (user == null)
            return null;

        if (!user.getPassword().equals(password))
            return null;

        return user;   // success
    }

    public boolean registerUser(String username, String password, String fullName, String email) {

        if (userDAO.getUserByUsername(username) != null) {
            System.out.println("Username already exists.");
            return false;
        }

        User newUser = new User(0, username, password, fullName, email);
        return userDAO.saveUser(newUser);
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }
}
