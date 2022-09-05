package org.library.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.account.ACCOUNT_TYPE;
import org.library.account.AUTH_TYPE;
import org.library.account.Account;
import org.library.account.AccountManagement;
import org.library.entity.Author;
import org.library.entity.Book;
import org.library.entity.BookItem;
import org.library.fine.CardTransaction;
import org.library.fine.Fine;
import org.library.notification.SMSNotification;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookManagementTest {

    Account librarian;
    Account member;
    Account member2;

    @BeforeEach
    public void registerAccount() {
        Account admin = new Account(1, "admin", "admin", "admin", ACCOUNT_TYPE.LIBRARIAN);
        AccountManagement management = new AccountManagement();

        Optional<Account> librarian = management.registerAccount(2, "librarian", "librarian", "librarian", ACCOUNT_TYPE.LIBRARIAN, admin);
        Optional<Account> member = management.registerAccount(3, "member", "member", "member", ACCOUNT_TYPE.MEMBER, librarian.get());
        Optional<Account> member2 = management.registerAccount(4, "member2", "member2", "member2", ACCOUNT_TYPE.MEMBER, librarian.get());
        this.librarian = librarian.get();
        this.member = member.get();
        this.member2 = member2.get();
    }

    @Test
    public void registerNewBook() {
        BookManagement management = new BookManagement();

        BookItem bookItem = new BookItem("123", new Book("ISBN_1", new Date(), "Title1", "My 1 Subject", "KOR", new Author("steven","love him")));

        Assertions.assertTrue(management.registerNewBook(bookItem, librarian));

        BookItem bookItem2 = new BookItem("321", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));

        Assertions.assertThrows(java.lang.SecurityException.class, ()-> management.registerNewBook(bookItem2, member));
    }

    @Test
    public void deleteABook() {

    }

    @Test
    public void findABook() {
        BookManagement management = new BookManagement();

        BookItem bookItem = new BookItem("123", new Book("ISBN_1", new Date(), "Title1", "My 1 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem2 = new BookItem("321", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));

        Assertions.assertTrue(management.registerNewBook(bookItem, librarian));

        Assertions.assertEquals(bookItem, management.findABook(bookItem.getBarcode(), librarian).get());
        Assertions.assertEquals(bookItem, management.findABook(bookItem.getBarcode(), member).get());

        Assertions.assertFalse(management.findABook(bookItem2.getBarcode(), librarian).isPresent());
        Assertions.assertFalse(management.findABook(bookItem2.getBarcode(), member).isPresent());
    }

    @Test
    public void searchCategory() {
        BookManagement management = new BookManagement();

        BookItem bookItem = new BookItem("123", new Book("ISBN_1", new Date(), "Title1", "My 1 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem2 = new BookItem("321", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem3 = new BookItem("456", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem4 = new BookItem("789", new Book("ISBN_3", new Date(), "Title3", "My 3 Subject", "KOR", new Author("superman","love him")));

        Assertions.assertTrue(management.registerNewBook(bookItem, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem2, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem3, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem4, librarian));

        List<Book> books = management.findBooks("Title2",null,null,member);

        Assertions.assertEquals(2, books.size());

        List<Book> books2 = management.findBooks(null,new Author("superman","love him"),null,member);

        Assertions.assertEquals(1, books2.size());

        List<Book> books3 = management.findBooks(null,null,"My 1 Subject",member);

        Assertions.assertEquals(1, books3.size());

        List<Book> books4 = management.findBooks("Title2",null,"My 1 Subject",member);

        Assertions.assertEquals(3, books4.size());
    }

    @Test
    public void lendBook(){
        BookManagement management = new BookManagement();

        BookItem bookItem = new BookItem("123", new Book("ISBN_1", new Date(), "Title1", "My 1 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem2 = new BookItem("321", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem3 = new BookItem("456", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem4 = new BookItem("789", new Book("ISBN_3", new Date(), "Title3", "My 3 Subject", "KOR", new Author("superman","love him")));

        Assertions.assertTrue(management.registerNewBook(bookItem, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem2, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem3, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem4, librarian));

        Assertions.assertTrue(management.checkout("123", member));
        Assertions.assertFalse(management.checkout("123", member2));
        Assertions.assertTrue(management.reserve("123", member2, new SMSNotification(member2, "book returned")));
        Assertions.assertTrue(management.returnBook("123", member));
    }

    @Test
    public void fineTest(){
        BookManagement management = new BookManagement();

        BookItem bookItem = new BookItem("123", new Book("ISBN_1", new Date(), "Title1", "My 1 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem2 = new BookItem("321", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem3 = new BookItem("456", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem4 = new BookItem("789", new Book("ISBN_3", new Date(), "Title3", "My 3 Subject", "KOR", new Author("superman","love him")));

        Assertions.assertTrue(management.registerNewBook(bookItem, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem2, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem3, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem4, librarian));

        Assertions.assertTrue(management.checkout("123", member));
        Calendar cal = Calendar.getInstance();
        cal.set(2022,7,30);

        bookItem.setDueDate(cal.getTime());
        Assertions.assertFalse(management.returnBook("123", member));

        Fine fine = management.generateFine("123", member);
        Assertions.assertNotNull(fine);

        fine.process(new CardTransaction(1234567));

        Assertions.assertEquals(BOOK_STATUS.AVAILABLE, bookItem.getStatus());
    }


    @Test
    public void expireNotificationTest(){
        BookManagement management = new BookManagement();

        BookItem bookItem = new BookItem("123", new Book("ISBN_1", new Date(), "Title1", "My 1 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem2 = new BookItem("321", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem3 = new BookItem("456", new Book("ISBN_2", new Date(), "Title2", "My 2 Subject", "KOR", new Author("steven","love him")));
        BookItem bookItem4 = new BookItem("789", new Book("ISBN_3", new Date(), "Title3", "My 3 Subject", "KOR", new Author("superman","love him")));

        Assertions.assertTrue(management.registerNewBook(bookItem, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem2, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem3, librarian));
        Assertions.assertTrue(management.registerNewBook(bookItem4, librarian));

        Assertions.assertTrue(management.checkout("123", member, new SMSNotification(member, "Book Lend Expire Approached")));

        Assertions.assertTrue(management.returnBook("123", member));

        Assertions.assertEquals(BOOK_STATUS.AVAILABLE, bookItem.getStatus());
    }
}