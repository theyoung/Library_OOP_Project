package org.library.book.search;

import org.library.entity.Book;
import org.library.entity.BookItem;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CategorySearchBySubject extends Category<String,BookItem,Book>{

    public CategorySearchBySubject(String subject) {
        super(subject);
    }

    @Override
    public List<Book> searchCategoryBy(Collection<BookItem> source) {
        List<Book> books = new LinkedList<>();

        for(BookItem bookItem : source){
            Optional<Book> result = bookItem.ofSubject(target);
            result.ifPresent(books::add);
        }
        return books;
    }
}
