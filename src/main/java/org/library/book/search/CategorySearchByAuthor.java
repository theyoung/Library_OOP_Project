package org.library.book.search;

import org.library.entity.Author;
import org.library.entity.Book;
import org.library.entity.BookItem;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CategorySearchByAuthor extends Category<Author,BookItem,Book>{

    public CategorySearchByAuthor(Author author) {
        super(author);
    }

    @Override
    public List<Book> searchCategoryBy(Collection<BookItem> source) {
        List<Book> books = new LinkedList<>();

        for(BookItem bookItem : source){
            Optional<Book> result = bookItem.ofAuthor(target);
            result.ifPresent(books::add);
        }
        return books;
    }
}
