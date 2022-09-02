package org.library.fine;

public class CardTransaction implements Transaction{
    long cardNumber;

    public CardTransaction(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(long amount) {
        System.out.println("Paid " + amount + " with Card : " + cardNumber);
        return true;
    }
}
