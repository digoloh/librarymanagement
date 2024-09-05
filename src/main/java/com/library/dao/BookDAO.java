// BookDAO.java
package com.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.library.config.DatabaseConfig;
import com.library.models.Book;

public class BookDAO {

    public void save(Book book) throws SQLException {
        String query = "INSERT INTO books (isbn, title, author, available, approved) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setBoolean(4, book.isAvailable());
            stmt.setBoolean(5, book.isApproved());
            stmt.executeUpdate();
        }
        catch(Exception e){
          e.printStackTrace();
        }
    }

    public void update(Book book) throws SQLException {
        String query = "UPDATE books SET available = ?, approved = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, book.isAvailable());
            stmt.setBoolean(2, book.isApproved());
            stmt.setInt(3, book.getBookId());
            stmt.executeUpdate();
        }
    }

    public List<Book> findAllApproved() throws SQLException {
        String query = "SELECT * FROM books WHERE approved = true";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("id"));
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setAvailable(rs.getBoolean("available"));
                book.setApproved(rs.getBoolean("approved"));
                books.add(book);
            }
        }
        return books;
    }

    public Book findById(int bookId) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("id"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setAvailable(rs.getBoolean("available"));
                    book.setApproved(rs.getBoolean("approved"));
                    return book;
                }
            }
        }
        return null;
    }
}
