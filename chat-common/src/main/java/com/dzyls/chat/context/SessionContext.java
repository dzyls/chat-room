package com.dzyls.chat.context;

import com.dzyls.chat.entity.Account;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2022/3/3 22:38
 * @Version 1.0.0
 * @Description: The runtime context,Provides a way to identify a user across more than one request
 */
public interface SessionContext {

    /**
     * Get the client address
     *
     * @return
     */
    public InetSocketAddress getRemote();

    /**
     * Set the client address
     *
     * @param address
     */
    public void setRemote(InetSocketAddress address);

    /**
     * Get the account associated with the request
     *
     * @return
     */
    public Account getAccount();

    /**
     * Set the account
     */
    public void setAccount(Account account);

    public long getKeepAliveTime();

    public void setKeepAliveTime(long aliveTime);

    /**
     * Set attribute,used in the context of connection.
     *
     * @param key
     * @param value
     */
    public void setParameter(String key, Object value);

    /**
     * Get the attribute in the context of runtime.
     *
     * @param key
     * @return if value of the key doesn't exist,then return null.
     */
    public Object getParameter(String key);

    /**
     * Add temporary key-value, will be lost after request.
     *
     * @param key
     * @param value
     */
    public void setAttribute(String key, Object value);

    void removeAttribute(String key);

    /**
     * Get temporary value.
     *
     * @param key
     * @return
     */
    public Object getAttribute(String key);

    /**
     * Clear the request temporary state.
     */
    public void clearRequestState();

    public void clearAttributes();

    /**
     * Get all the attributes
     *
     * @return
     */
    public Map<String, Object> getAllParameters();

}
