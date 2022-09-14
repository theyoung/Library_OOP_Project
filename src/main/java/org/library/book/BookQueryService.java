package org.library.book;

import org.library.book.search.Category;
import org.library.entity.Book;
import org.library.entity.BookItem;

import java.util.List;

public class BookQueryService implements CategorySearch<Book> {
    List<BookItem> wholeBookItems;

    public BookQueryService(List<BookItem> wholeBookItems) {
        this.wholeBookItems = wholeBookItems;
    }

    @Override
    public List<Book> searchBookBy(Category category) {
        return category.searchCategoryBy(wholeBookItems);
    }
}
