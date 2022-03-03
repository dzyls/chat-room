package com.dzyls.chat.context.impl;

import com.dzyls.chat.context.SessionContext;
import com.dzyls.chat.entity.Account;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2022/3/3 22:44
 * @Version 1.0.0
 * @Description:
 */
public class DefaultSessionContext implements SessionContext {

    /**
     * Store all the attributes associated with the request.
     */
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    private static final ThreadLocal<Map<String, Object>> localParams = ThreadLocal.withInitial(HashMap::new);

    /**
     * Remote address.
     */
    private InetSocketAddress remoteAddress;

    /**
     * Account associated with the session.
     */
    private Account account;

    protected long aliveTime;

    /**
     * Default constructor
     */
    public DefaultSessionContext() {

    }

    /**
     * @param account
     * @param remoteAddress
     */
    public DefaultSessionContext(Account account,
                                 InetSocketAddress remoteAddress) {
        this.account = account;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public Object getParameter(String key) {
        return localParams.get().get(key);
    }

    @Override
    public void setParameter(String key, Object value) {
        localParams.get().put(key, value);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public void clearRequestState() {
        localParams.get().clear();
    }

    @Override
    public void clearAttributes() {
        attributes.clear();
    }

    @Override
    public InetSocketAddress getRemote() {
        return remoteAddress;
    }

    @Override
    public void setRemote(InetSocketAddress address) {
        remoteAddress = address;
    }

    @Override
    public Map<String, Object> getAllParameters() {
        return localParams.get();
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }


    @Override
    public long getKeepAliveTime() {
        return aliveTime;
    }

    @Override
    public void setKeepAliveTime(long aliveTime) {
        this.aliveTime = aliveTime;
    }

}
