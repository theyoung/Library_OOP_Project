package org.library.book;

import org.library.account.Account;
import org.library.entity.BookItem;
import org.library.fine.Fine;
import org.library.notification.NotificationCallback;

public interface LendBook<T> {
    boolean checkout(T bookItem, Account account);
    boolean reserve(T bookItem, Account account, NotificationCallback callback);
    boolean returnBook(T bookItem, Account account);
    Fine issueFine(T bookItem, Account account);
}
