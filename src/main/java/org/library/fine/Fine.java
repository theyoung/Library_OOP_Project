package org.library.fine;

import org.library.entity.BookItem;

import java.util.Date;

public class Fine {
    long amount;
    BookItem bookItem;
    TransactionCallback callback;

    private Fine(long amount, BookItem bookItem, TransactionCallback callback){
        this.amount = amount;
        this.bookItem = bookItem;
        this.callback = callback;
    }

    public boolean process(Transaction transaction){
        if(transaction.pay(amount)){
            callback.next();
            return true;
        }
        return false;
    }

    public static Fine fineCalculate(BookItem bookItem, TransactionCallback callback){
        return new Fine(bookItem.timeDiff() / 1000, bookItem, callback);
    }
}
