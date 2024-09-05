package com.library;

import com.library.dao.BookDAO;
import com.library.dao.UserDAO;
import com.library.models.Book;
import com.library.models.User;
import com.library.services.LibraryService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        LibraryService libraryService = new LibraryService();

        try {
            User user = libraryService.login("jane_doe", "securepassword"); // assuming login returns a User object
            libraryService.borrowBook(user, 2); // Attempt to borrow the book with ID 3
            System.out.println("Book borrowed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to borrow book.");
        }
    }
}