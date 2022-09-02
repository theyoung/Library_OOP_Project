package org.library.account;

import java.util.Map;

public enum ACCOUNT_TYPE {
    LIBRARIAN,
    MEMBER;

    public Map<AUTH_TYPE, Boolean> getAuthorizations(){
        switch(this){
            case LIBRARIAN :
                return Map.of(AUTH_TYPE.ADD_BOOK, true,AUTH_TYPE.DEL_BOOK, true, AUTH_TYPE.FIND_BOOK, true);
            case MEMBER:
                return Map.of(AUTH_TYPE.ADD_BOOK, false,AUTH_TYPE.DEL_BOOK, false, AUTH_TYPE.FIND_BOOK, true, AUTH_TYPE.CHECK_OUT, true);
        }
        return Map.of();
    }
}
