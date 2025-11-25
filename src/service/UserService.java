package service;

import db.UserDAO;
import model.User;

/**
 * Simple user authentication/registration service backed by SQLite.
 * This keeps the UI decoupled from DAO details.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Attempt login with name/password.
     * @return User on success, null on failure.
     */
    public User login(String name, String password) {
        User user = userDAO.findByName(name);
        if (user == null) {
            return null;
        }
        return user.getPassword().equals(password) ? user : null;
    }

    /**
     * Register a new user if the name is not taken.
     */
    public boolean register(String name, String password) {
        if (userDAO.findByName(name) != null) {
            return false;
        }
        return userDAO.insertUser(new User(name, password));
    }
}
