package org.library.entity;

import org.library.book.BOOK_STATUS;
import org.library.book.BookManagement;
import org.library.annotations.Entity;
import org.library.annotations.Transient;
import org.library.notification.NotificationCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Entity
public class BookItem {
    String barcode;
    Date borrowed;
    Date dueDate;
    BOOK_STATUS status;
    Book book;

    //reserve 시 callback
    @Transient
    NotificationCallback reserveCallback;

    public BookItem(String barcode, Book book) {
        this.barcode = barcode;
        this.status = BOOK_STATUS.AVAILABLE;
        this.book = book;
    }

    public String getTitle(){
        return book.getTitle();
    }

    // to test purpose only
    public BOOK_STATUS getStatus(){
        return status;
    }

    public Optional<Book> ofTitle(String title) {
        if(book.title.equals(title)) return Optional.of(book);
        return Optional.empty();
    }

    public Optional<Book> ofAuthor(Author author) {
        if(book.author.equals(author)) return Optional.of(book);
        return Optional.empty();
    }

    public Optional<Book> ofSubject(String subject) {
        if(book.subject.equals(subject)) return Optional.of(book);
        return Optional.empty();
    }


    @Override
    public boolean equals(Object targetBookItem) {
        if(targetBookItem instanceof String) {
            return this.barcode.equals(targetBookItem);
        }

        if(!(targetBookItem instanceof BookItem)) return false;
        return this.barcode.equals(((BookItem) targetBookItem).getBarcode());
    }

    public String getBarcode(){
        return this.barcode;
    }

    public boolean checkout(){
        if(status != BOOK_STATUS.AVAILABLE) return false;

        borrowed = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(borrowed);
        cal.add(Calendar.DATE, BookManagement.MAX_LEND_DAYS);
        dueDate = cal.getTime();

        status = BOOK_STATUS.LEND;

        return true;
    }

    public boolean reserve(NotificationCallback notification){
        if(status != BOOK_STATUS.LEND) return false;
        if(notification != null) this.reserveCallback = notification;

        status = BOOK_STATUS.RESERVED;
        return true;
    }

    public boolean returnBook(){
        if(status == BOOK_STATUS.AVAILABLE) return false;

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        Calendar due = Calendar.getInstance();
        due.setTime(this.dueDate);

        if(cal.after(due)){
            System.out.println("You Have to Clear Fine");
            return false;
        }

        forceCheckout();
        return true;
    }

    public void forceCheckout(){
        borrowed = null;
        dueDate = null;
        status = BOOK_STATUS.AVAILABLE;
        if(reserveCallback != null) reserveCallback.triggerStatusChanged(this);
        reserveCallback = null;
    }

    public long timeDiff(){
        return (new Date()).getTime() - this.dueDate.getTime();
    }

    //this method should be deleted
    //the purpose is testing only
    public void setDueDate(Date date){
        this.dueDate = date;
    }
}
