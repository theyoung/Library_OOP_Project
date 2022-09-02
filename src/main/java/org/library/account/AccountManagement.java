package org.library.account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountManagement {
    Map<Integer, Account> accountMap;

    public AccountManagement() {
        this.accountMap = new HashMap<>();
    }

    public Optional<Account> registerAccount(Integer id, String password, String name, String address, ACCOUNT_TYPE type, Account requester) throws SecurityException{
        if(requester == null || requester.type == ACCOUNT_TYPE.MEMBER) {
            throw new SecurityException("Requester must be librarian to register member");
        }
        Account account = new Account(id,password,name,address,type);
        accountMap.put(id, account);
        return Optional.of(account);
    }
}
