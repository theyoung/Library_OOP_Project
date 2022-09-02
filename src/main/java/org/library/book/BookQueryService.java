package org.library.book;

import org.library.entity.Author;
import org.library.entity.Book;
import org.library.entity.BookItem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BookQueryService implements CategorySearch<Book>{
    List<BookItem> wholeBookItems;

    public BookQueryService(List<BookItem> wholeBookItems) {
        this.wholeBookItems = wholeBookItems;
    }

    @Override
    public List<Book> searchBookByTitle(String title) {
        List<Book> books = new LinkedList<>();

        for(BookItem bookItem : wholeBookItems){
            Optional<Book> result = bookItem.ofTitle(title);
            result.ifPresent(books::add);
        }
        return books;
    }

    @Override
    public List<Book> searchBookByAuthor(Author author) {
        List<Book> books = new LinkedList<>();

        for(BookItem bookItem : wholeBookItems){
            Optional<Book> result = bookItem.ofAuthor(author);
            result.ifPresent(books::add);
        }
        return books;
    }

    @Override
    public List<Book> searchBookBySubject(String subject) {
        List<Book> books = new LinkedList<>();

        for(BookItem bookItem : wholeBookItems){
            Optional<Book> result = bookItem.ofSubject(subject);
            result.ifPresent(books::add);
        }
        return books;
    }
}
