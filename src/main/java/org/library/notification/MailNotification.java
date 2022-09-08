package org.library.notification;

import org.library.account.Account;
import org.library.entity.BookItem;

public class MailNotification implements NotificationCallback<BookItem>{
    Account account;
    String message;

    public MailNotification(Account account, String message) {
        this.account = account;
        this.message = message;
    }

    @Override
    public void triggerStatusChanged(BookItem bookItem) {
        System.out.println("Mail Send to = " + account.getName() + " with " + message + "/ The book is ");
        bookItem.printTitle();
    }
}
