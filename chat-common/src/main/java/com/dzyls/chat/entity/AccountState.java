package com.dzyls.chat.entity;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2022/3/3 22:47
 * @Version 1.0.0
 * @Description: Account State
 */
public enum AccountState {

    ACTIVE(1),
    SUSPEND(2),
    BLOCKED(3);

    int index;

    AccountState(int state) {
        this.index = state;
    }

    public AccountState getAccountState(int i){
        for (AccountState accountState : AccountState.values()) {
            if (accountState.index == i){
                return accountState;
            }
        }
        throw new IllegalStateException("Unexpected Account State :"+i);
    }

    public int getEnumIndex(AccountState accountState){
        return accountState.index;
    }

}
