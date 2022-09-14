package org.library.book;

import org.library.account.AUTH_TYPE;
import org.library.account.Account;
import org.library.book.search.CategorySearchByAuthor;
import org.library.book.search.CategorySearchBySubject;
import org.library.book.search.CategorySearchByTitle;
import org.library.entity.Author;
import org.library.entity.Book;
import org.library.entity.BookItem;
import org.library.fine.Fine;
import org.library.notification.NotificationCallback;

import java.util.*;
import java.util.stream.Collectors;

public class BookManagement{
    public static final Integer MAX_LEND_DAYS = 10;
    public static final Integer MAX_LEND_BOOKS = 5;

    BookInOut<BookItem, BookItem> bookCollection;
    CategorySearch<Book> category;
    LendBook<BookItem> lendBook;

    public BookManagement() {
        //for concurrency
        List<BookItem> wholeBookItems = Collections.synchronizedList(new LinkedList<>());

        //Dependency Injection 처리 필요함
        bookCollection = new BookItemRepository<>(wholeBookItems);
        category = new BookQueryService(wholeBookItems);
        lendBook = new LendBookService();
    }

    //dependency injection을 위한 생성자
    public BookManagement(BookInOut<BookItem, BookItem> bookItemRepository, CategorySearch<Book> queryService, LendBook<BookItem> lendBookService) {
        //for concurrency
        List<BookItem> wholeBookItems = Collections.synchronizedList(new LinkedList<>());

        bookCollection = bookItemRepository;
        category = queryService;
        lendBook =  lendBookService;
    }

    //조회 결과가 많을 경우 매우 좋지 않은 퍼포먼스를 보임
    //개별 조회로 분리 하는게 좋을 지도...
    //And 조건이 맞았을 것 같긴한데 귀찮으니까 Or 조건으로 처리
    public List<Book> findBooks(String title, Author author,String subject, Account account) throws SecurityException{
        if(!account.checkAuthorizations(AUTH_TYPE.FIND_BOOK)) {
            throw new SecurityException(account.getName() + " Account does not have a right to fin a book");
        }

        List<Book> result = new LinkedList<>();

        if(title != null) result.addAll(category.searchBookBy(new CategorySearchByTitle(title)));
        if(author != null) result.addAll(category.searchBookBy(new CategorySearchByAuthor(author)));
        if(subject != null) result.addAll(category.searchBookBy(new CategorySearchBySubject(subject)));

        return result.stream().distinct().collect(Collectors.toList());
    }

    public boolean registerNewBook(BookItem bookItem, Account account) throws SecurityException{
        if(!account.checkAuthorizations(AUTH_TYPE.ADD_BOOK)) {
            throw new SecurityException(account.getName() + " Account does not have a right to register a new book");
        }

        return bookCollection.addBook(bookItem);
    }

    public Optional<BookItem> findABook(String barcode, Account account) throws SecurityException{
        if(!account.checkAuthorizations(AUTH_TYPE.FIND_BOOK)) {
            throw new SecurityException(account.getName() + " Account does not have a right to fin a book");
        }

        return bookCollection.searchBookItem(barcode);
    }

    public boolean checkout(String barcode, Account account) throws SecurityException {
        if(!account.checkAuthorizations(AUTH_TYPE.CHECK_OUT)) {
            throw new SecurityException(account.getName() + " Account does not have a right to check out a book");
        }
        Optional<BookItem> rendBook = bookCollection.searchBookItem(barcode);
        if(rendBook.isEmpty()) return false;

        return lendBook.checkout(rendBook.get(), account);
    }

    public boolean checkout(String barcode, Account account, NotificationCallback callback) throws SecurityException {
        if(!account.checkAuthorizations(AUTH_TYPE.CHECK_OUT)) {
            throw new SecurityException(account.getName() + " Account does not have a right to check out a book");
        }
        Optional<BookItem> rendBook = bookCollection.searchBookItem(barcode);
        if(rendBook.isEmpty()) return false;

        return lendBook.checkoutWithNotification(rendBook.get(), account, callback);
    }

    public boolean reserve(String barcode, Account account, NotificationCallback callback) throws SecurityException {
        if(!account.checkAuthorizations(AUTH_TYPE.CHECK_OUT)) {
            throw new SecurityException(account.getName() + " Account does not have a right to check out a book");
        }
        Optional<BookItem> rendBook = bookCollection.searchBookItem(barcode);
        if(rendBook.isEmpty()) return false;

        return lendBook.reserve(rendBook.get(), account, callback);
    }

    public boolean returnBook(String barcode, Account account) throws SecurityException {
        Optional<BookItem> rendBook = bookCollection.searchBookItem(barcode);
        if(rendBook.isEmpty()) return false;

        return lendBook.returnBook(rendBook.get(), account);
    }

    public Fine generateFine(String barcode, Account account) throws SecurityException {
        Optional<BookItem> rendBook = bookCollection.searchBookItem(barcode);
        if(rendBook.isEmpty()) return null;

        return lendBook.issueFine(rendBook.get(), account);
    }

}
