// LibraryController.java
package com.library.controllers;

import com.library.models.User;
import com.library.dao.BookDAO;
import com.library.dao.BorrowRecordDAO;
import com.library.dao.UserDAO;
import com.library.models.Book;
import com.library.services.LibraryService;

import java.util.Scanner;

public class LibraryController {
    private final LibraryService libraryService;

    // Constructor
    public LibraryController() {
        // Create instances of the required DAOs
        UserDAO userDAO = new UserDAO();  // Ensure these have appropriate constructors
        BookDAO bookDAO = new BookDAO();
        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

        // Instantiate the LibraryService with the required parameters
        this.libraryService = new LibraryService(userDAO, bookDAO, borrowRecordDAO);
    }
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            System.out.println("\n1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Search Books");
            System.out.println("4. Borrow Book");
            System.out.println("5. Upload Book (Admin)");
            System.out.println("6. Approve Book (Admin)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1: signup(); break;
                case 2: login(); break;
                case 3: searchBooks(); break;
                case 4: borrowBook(); break;
                case 5: uploadBook(); break;
                case 6: approveBook(); break;
                case 7: System.exit(0);
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void signup() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Enter role (admin/user): ");
        String role = scanner.nextLine();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        try {
            libraryService.signup(user);
            System.out.println("User registered successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void login() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = libraryService.login(username, password);
            System.out.println("Login successful! Welcome, " + user.getUsername());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchBooks() {
        try {
            libraryService.searchBooks().forEach(book -> {
                System.out.println("ID: " + book.getBookId() +
                    ", Title: " + book.getTitle() +
                    ", Author: " + book.getAuthor() +
                    ", Available: " + book.isAvailable());
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void borrowBook() {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Enter book ID: ");
        int bookId = scanner.nextInt();

        try {
            User user = libraryService.login(username, "dummy");  // authenticate user
            libraryService.borrowBook(user, bookId);
            System.out.println("Book borrowed successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void uploadBook() {
        System.out.println("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.println("Enter title: ");
        String title = scanner.nextLine();
        System.out.println("Enter author: ");
        String author = scanner.nextLine();

        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);

        try {
            libraryService.uploadBook(book);
            System.out.println("Book uploaded successfully! Pending approval.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void approveBook() {
        System.out.println("Enter book ID to approve: ");
        int bookId = scanner.nextInt();

        try {
            libraryService.approveBook(bookId);
            System.out.println("Book approved successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
