// BorrowRecordDAO.java
package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.BorrowRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowRecordDAO {

    public void save(BorrowRecord record) throws SQLException {
        String query = "INSERT INTO borrow_records (user_id, book_id, borrow_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, record.getUserId());
            stmt.setInt(2, record.getBookId());
            stmt.setDate(3, new java.sql.Date(record.getBorrowDate().getTime()));
            stmt.executeUpdate();
        }
    }
    public void deleteBorrowRecord(int userId, int bookId) throws SQLException {
        String query = "DELETE FROM borrow_records WHERE user_id = ? AND book_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    public BorrowRecord findByUserAndBook(int userId, int bookId) throws SQLException {
        String query = "SELECT * FROM borrow_records WHERE user_id = ? AND book_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BorrowRecord record = new BorrowRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setUserId(rs.getInt("user_id"));
                    record.setBookId(rs.getInt("book_id"));
                    record.setBorrowDate(rs.getDate("borrow_date"));
                    return record;
                }
            }
        }
        return null;
    }
}
