// LibraryServiceTest.java
package com.library.services;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.library.dao.BookDAO;
import com.library.dao.BorrowRecordDAO;
import com.library.dao.UserDAO;
import com.library.models.Book;
import com.library.models.BorrowRecord;
import com.library.models.User;



import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class LibraryServiceTest {

    private LibraryService libraryService;
    private UserDAO userDAO;
    private BookDAO bookDAO;
    private BorrowRecordDAO borrowRecordDAO;

    

    @Before
    public void setUp() {
        userDAO = mock(UserDAO.class);
        bookDAO = mock(BookDAO.class);
        borrowRecordDAO = mock(BorrowRecordDAO.class);
        libraryService = new LibraryService(userDAO, bookDAO, borrowRecordDAO);
    }

    @Test
    public void testSignup() throws SQLException {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole("user");

        libraryService.signup(user);

        verify(userDAO, times(1)).save(user);
    }

    @Test
    public void testLoginSuccess() throws SQLException, Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");

        when(userDAO.findByUsername("testuser")).thenReturn(user);

        User loggedInUser = libraryService.login("testuser", "testpassword");

        assertEquals("testuser", loggedInUser.getUsername());
    }

    @Test(expected = Exception.class)
    public void testLoginFailure() throws SQLException, Exception {
        when(userDAO.findByUsername("testuser")).thenReturn(null);

        libraryService.login("testuser", "wrongpassword");
    }

    @Test
    public void testSearchBooks() throws SQLException {
        Book book1 = new Book();
        book1.setTitle("Java Programming");
        book1.setAuthor("John Doe");

        Book book2 = new Book();
        book2.setTitle("Python Programming");
        book2.setAuthor("Jane Doe");

        List<Book> books = Arrays.asList(book1, book2);

        when(bookDAO.findAllApproved()).thenReturn(books);

        List<Book> result = libraryService.searchBooks();

        assertEquals(2, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
    }

    @Test
    public void testBorrowBookSuccess() throws SQLException, Exception {
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        Book book = new Book();
        book.setBookId(1);
        book.setAvailable(true);

        when(bookDAO.findById(1)).thenReturn(book);

        libraryService.borrowBook(user, 1);

        verify(bookDAO, times(1)).update(book);
        verify(borrowRecordDAO, times(1)).save(any(BorrowRecord.class));

        assertFalse(book.isAvailable());
    }

    @Test(expected = Exception.class)
    public void testBorrowBookFailure() throws SQLException, Exception {
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        Book book = new Book();
        book.setBookId(1);
        book.setAvailable(false);

        when(bookDAO.findById(1)).thenReturn(book);

        libraryService.borrowBook(user, 1);
    }

    @Test
    public void testUploadBook() throws SQLException {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
        book.setAuthor("Test Author");

        libraryService.uploadBook(book);

        verify(bookDAO, times(1)).save(book);
        assertFalse(book.isApproved());
        assertTrue(book.isAvailable());
    }

    @Test
    public void testApproveBook() throws SQLException, Exception {
        Book book = new Book();
        book.setBookId(1);
        book.setApproved(false);

        when(bookDAO.findById(1)).thenReturn(book);

        libraryService.approveBook(1);

        verify(bookDAO, times(1)).update(book);
        assertTrue(book.isApproved());
    }
}
