package org.library.book;

import org.library.account.Account;
import org.library.entity.BookItem;
import org.library.fine.Fine;
import org.library.notification.NotificationCallback;
import org.library.notification.SMSNotification;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LendBookService implements LendBook<BookItem>{
    Map<Account, List<BookItem>> lendStatus;
    Map<Account, List<BookItem>> reserveStatus;

    public LendBookService() {
        this.lendStatus = new ConcurrentHashMap<>();
        this.reserveStatus = new ConcurrentHashMap<>();
    }

    @Override
    public boolean checkout(BookItem bookItem, Account account) {
        List<BookItem> lendBooks = lendStatus.getOrDefault(account, new LinkedList<>());
        if(lendBooks.size() >= BookManagement.MAX_LEND_BOOKS){
            System.out.println("Exceed Lend Book Count");
            return false;
        }

        if (bookItem.checkout()) {
            lendBooks.add(bookItem);
            lendStatus.put(account, lendBooks);
            return true;
        }
        System.out.println("A book checked out already");
        return false;
    }

    @Override
    public boolean reserve(BookItem bookItem, Account account, NotificationCallback callback) {
        if (bookItem.reserve(callback)) {
            List<BookItem> reserveBooks = reserveStatus.getOrDefault(account, new LinkedList<>());
            reserveBooks.add(bookItem);
            reserveStatus.put(account, reserveBooks);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnBook(BookItem bookItem, Account account) {
        List<BookItem> lendBooks = lendStatus.get(account);
        for(BookItem lent : lendBooks){
            if(lent.equals(bookItem)){
                if (bookItem.returnBook()) {
                    break;
                } else {
                    //remained Fine
                    return false;
                }
            }
        }
        return lendBooks.removeIf(bookItem::equals);
    }

    private boolean forceReturnBook(BookItem bookItem, Account account) {
        List<BookItem> lendBooks = lendStatus.get(account);
        for(BookItem lent : lendBooks){
            if(lent.equals(bookItem)){
                bookItem.forceCheckout();
            }
        }
        return lendBooks.removeIf(bookItem::equals);
    }

    @Override
    public Fine issueFine(BookItem bookItem, Account account) {
        return Fine.fineCalculate(bookItem, ()->{
            System.out.println("Paid Fine");
            forceReturnBook(bookItem, account);
        });
    }


}
