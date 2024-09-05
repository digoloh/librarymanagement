// UserService.java
package com.library.services;

import com.library.dao.UserDAO;
import com.library.models.User;

import java.sql.SQLException;

public abstract class UserService {
    protected UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public abstract void borrowBook(int userId, int bookId) throws SQLException, Exception;

    public abstract void returnBook(int userId, int bookId) throws SQLException, Exception;

    public void signup(User user) throws SQLException {
        userDAO.save(user);
    }

    public User login(String username, String password) throws SQLException, Exception {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new Exception("Invalid credentials");
    }
}
