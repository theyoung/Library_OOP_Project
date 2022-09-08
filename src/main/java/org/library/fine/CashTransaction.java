package org.library.fine;

public class CashTransaction implements Transaction{

    long save;

    public CashTransaction(long save) {
        this.save = save;
    }

    @Override
    public boolean pay(long amount) {
        if(this.save > amount){
            System.out.println("Payment Finish");
            return true;
        } else {
            System.out.println("Lack of cash" + (amount - this.save));
            return false;
        }
    }
}
