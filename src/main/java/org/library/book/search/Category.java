package org.library.book.search;

import java.util.Collection;
import java.util.List;

public abstract class Category<T,E,R> {
    T target;
    Category(T target){
        this.target = target;
    }

    abstract public List<R> searchCategoryBy(Collection<E> source);
}
