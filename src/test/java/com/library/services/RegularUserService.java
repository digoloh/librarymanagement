// RegularUserService.java
package com.library.services;

import com.library.dao.BookDAO;
import com.library.dao.BorrowRecordDAO;
import com.library.dao.UserDAO;
import com.library.models.Book;
import com.library.models.BorrowRecord;

import java.sql.SQLException;
import java.util.Date;

public class RegularUserService extends UserService {
    private BookDAO bookDAO;
    private BorrowRecordDAO borrowRecordDAO;

    public RegularUserService(UserDAO userDAO, BookDAO bookDAO, BorrowRecordDAO borrowRecordDAO) {
        super(userDAO);
        this.bookDAO = bookDAO;
        this.borrowRecordDAO = borrowRecordDAO;
    }

    @Override
    public void borrowBook(int userId, int bookId) throws SQLException, Exception {
        Book book = bookDAO.findById(bookId);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            bookDAO.update(book);

            BorrowRecord record = new BorrowRecord();
            record.setUserId(userId);
            record.setBookId(bookId);
            record.setBorrowDate(new Date());
            borrowRecordDAO.save(record);
        } else {
            throw new Exception("Book not available");
        }
    }

    @Override
    public void returnBook(int userId, int bookId) throws SQLException, Exception {
        Book book = bookDAO.findById(bookId);
        if (book != null) {
            book.setAvailable(true);
            bookDAO.update(book);

            BorrowRecord record = borrowRecordDAO.findByUserAndBook(userId, bookId);
            if (record != null) {
                borrowRecordDAO.deleteBorrowRecord(userId, bookId);
            } else {
                throw new Exception("Borrow record not found");
            }
        } else {
            throw new Exception("Book not found");  
        }
    }
}
