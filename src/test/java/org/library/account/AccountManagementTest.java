package org.library.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class AccountManagementTest {

    @Test
    public void registerAccount() {
        Account admin = new Account(1, "admin", "admin", "admin", ACCOUNT_TYPE.LIBRARIAN);
        AccountManagement management = new AccountManagement();

        Optional<Account> librarian = management.registerAccount(2, "librarian", "librarian", "librarian", ACCOUNT_TYPE.LIBRARIAN, admin);
        Optional<Account> member = management.registerAccount(3, "member", "member", "member", ACCOUNT_TYPE.MEMBER, librarian.get());

        Assertions.assertNotNull(librarian.get());
        Assertions.assertNotNull(member.get());
        Assertions.assertFalse(member.get().checkAuthorizations(AUTH_TYPE.ADD_BOOK));
        Assertions.assertTrue(librarian.get().checkAuthorizations(AUTH_TYPE.ADD_BOOK));

        Assertions.assertThrows(SecurityException.class, () -> management.registerAccount(4, "except", "except", "except", ACCOUNT_TYPE.MEMBER, member.get()));
        Assertions.assertThrows(SecurityException.class, () -> management.registerAccount(4, "except", "except", "except", ACCOUNT_TYPE.MEMBER, null));
    }

}