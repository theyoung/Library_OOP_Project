package org.library.book;

import org.library.entity.BookItem;

import java.util.List;
import java.util.Optional;

public class BookItemRepository<T> implements BookInOut <T,T> {

    List<T> wholeBookItems;

    public BookItemRepository(List<T> wholeBookItems) {
        this.wholeBookItems = wholeBookItems;
    }

    @Override
    public boolean addBook(T bookItem) {
        wholeBookItems.add(bookItem);
        return true;
    }

    @Override
    public boolean removeBook(T bookItem) {
        return wholeBookItems.removeIf(bookItem::equals);
    }

    @Override
    public Optional<T> searchBookItem(String barcode) {
        return wholeBookItems.stream().filter((bookItem)->bookItem.equals(barcode)).findFirst();
    }
}
