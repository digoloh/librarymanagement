// AdminUserService.java
package com.library.services;

import com.library.dao.BookDAO;
import com.library.dao.UserDAO;
import com.library.models.Book;

import java.sql.SQLException;

public class AdminUserService extends UserService {
    private BookDAO bookDAO;

    public AdminUserService(UserDAO userDAO, BookDAO bookDAO) {
        super(userDAO);
        this.bookDAO = bookDAO;
    }

    @Override
    public void borrowBook(int userId, int bookId) {
        // Admins may not borrow books; throw an exception or provide different behavior.
        throw new UnsupportedOperationException("Admins cannot borrow books.");
    }

    @Override
    public void returnBook(int userId, int bookId) {
        // Admins may not return books; throw an exception or provide different behavior.
        throw new UnsupportedOperationException("Admins cannot return books.");
    }

    public void approveBook(int bookId) throws SQLException, Exception {
        Book book = bookDAO.findById(bookId);
        if (book != null) {
            book.setApproved(true);
            bookDAO.update(book);
        } else {
            throw new Exception("Book not found");
        }
    }

    public void uploadBook(Book book) throws SQLException {
        book.setAvailable(true);
        book.setApproved(false);
        bookDAO.save(book);
    }
}
