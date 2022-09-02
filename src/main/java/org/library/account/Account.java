package org.library.account;

import java.util.Map;

public class Account {
    Integer id;
    String password;
    String name;
    String address;
    ACCOUNT_TYPE type;
    Map<AUTH_TYPE, Boolean> authorizations;

    public Account(Integer id, String password, String name, String address, ACCOUNT_TYPE type) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.address = address;
        this.type = type;
        authorizations = type.getAuthorizations();
    }

    public boolean checkAuthorizations(AUTH_TYPE authorization){
        if(this.authorizations.containsKey(authorization)) return this.authorizations.get(authorization);
        return false;
    }

    public String getName(){
        return this.name;
    }
}
