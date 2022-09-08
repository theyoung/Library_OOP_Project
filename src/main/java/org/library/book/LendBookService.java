package org.library.book;

import org.library.account.Account;
import org.library.entity.BookItem;
import org.library.fine.Fine;
import org.library.notification.NotificationCallback;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LendBookService implements LendBook<BookItem>{
    private static final Integer PRE_EXPIRE_ALERT_DAYS = 1;
    Map<Account, List<BookItem>> lendStatus;
    Map<Account, List<BookItem>> reserveStatus;
    Map<BookItem, Timer> expireAlerts;

    public LendBookService() {
        this.lendStatus = new ConcurrentHashMap<>();
        this.reserveStatus = new ConcurrentHashMap<>();
        this.expireAlerts = new ConcurrentHashMap<>();
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
    public boolean checkoutWithNotification(BookItem bookItem, Account account, NotificationCallback notification) {
        if(checkout(bookItem, account)){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    notification.triggerStatusChanged(bookItem);
                }
            };
            LocalDateTime now = LocalDateTime.now();
            now.plus(BookManagement.MAX_LEND_DAYS - PRE_EXPIRE_ALERT_DAYS, ChronoUnit.DAYS);
            timer.schedule(task, Timestamp.valueOf(now));

            expireAlerts.put(bookItem, timer);
            return true;
        }
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
        cancelAlert(bookItem);
        return lendBooks.removeIf(bookItem::equals);
    }

    private boolean forceReturnBook(BookItem bookItem, Account account) {
        List<BookItem> lendBooks = lendStatus.get(account);
        for(BookItem lent : lendBooks){
            if(lent.equals(bookItem)){
                bookItem.forceCheckout();
            }
        }
        cancelAlert(bookItem);
        return lendBooks.removeIf(bookItem::equals);
    }

    private void cancelAlert(BookItem bookItem){
        if (expireAlerts.get(bookItem) != null) {
            expireAlerts.get(bookItem).cancel();
            expireAlerts.remove(bookItem);
            System.out.println("Disabled Expire Alert for ");
            bookItem.printTitle();
        }

    }

    @Override
    public Fine issueFine(BookItem bookItem, Account account) {
        return Fine.fineCalculate(bookItem, ()->{
            System.out.println("Paid Fine");
            forceReturnBook(bookItem, account);
        });
    }


}
