package org.library.book;

import org.library.entity.Author;
import org.library.entity.Book;
import org.library.entity.BookItem;

import java.util.List;
import java.util.Optional;

public interface CategorySearch<T> {
    List<T> searchBookByTitle(String title);
    List<T> searchBookByAuthor(Author author);
    List<T> searchBookBySubject(String subject);
}
