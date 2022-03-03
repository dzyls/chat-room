package com.dzyls.chat.entity;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2022/3/3 22:42
 * @Version 1.0.0
 * @Description: Account
 */
public class Account {

    private String username;

    private String uid;

    private AccountState accountState;

    private long lastLoginTime;

    private String sign;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
