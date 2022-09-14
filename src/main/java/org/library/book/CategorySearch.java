package org.library.book;

import org.library.book.search.Category;

import java.util.List;

public interface CategorySearch<T> {
    List<T> searchBookBy(Category category);
}
