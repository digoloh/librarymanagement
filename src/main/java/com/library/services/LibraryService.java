// LibraryService.java
package com.library.services;

import com.library.dao.BookDAO;
import com.library.dao.BorrowRecordDAO;
import com.library.dao.UserDAO;
import com.library.models.Book;
import com.library.models.BorrowRecord;
import com.library.models.User;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class LibraryService {
	private UserDAO userDAO;
    private BookDAO bookDAO;
    private BorrowRecordDAO borrowRecordDAO;

    // Constructor with parameters
    public LibraryService(UserDAO userDAO, BookDAO bookDAO, BorrowRecordDAO borrowRecordDAO) {
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.borrowRecordDAO = borrowRecordDAO;
    }

    // Default constructor
    public LibraryService() {
        this.userDAO = new UserDAO();
        this.bookDAO = new BookDAO();
        this.borrowRecordDAO = new BorrowRecordDAO();
    }
    
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

    public List<Book> searchBooks() throws SQLException {
        return bookDAO.findAllApproved();
    }

    public void borrowBook(User user, int bookId) throws SQLException, Exception {
        Book book = bookDAO.findById(bookId);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            bookDAO.update(book);
            
            BorrowRecord record = new BorrowRecord();
            record.setUserId(user.getUserId());
            record.setBookId(bookId);
            record.setBorrowDate(new Date());
            borrowRecordDAO.save(record);
        } else {
            throw new Exception("Book not available");
        }
    }

    public void uploadBook(Book book) throws SQLException {
        book.setAvailable(true);
        book.setApproved(false);
        bookDAO.save(book);
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
}
