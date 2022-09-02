package org.library.book;

import org.library.entity.BookItem;

import java.util.Optional;

public interface BookInOut <T,R>{
    boolean addBook(T bookItem);

    boolean removeBook(T bookItem);

    Optional<R> searchBookItem(String barcode);
}
