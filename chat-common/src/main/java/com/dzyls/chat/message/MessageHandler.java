package com.dzyls.chat.message;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/22 15:41
 * @Version 1.0.0
 * @Description:
 */
public interface MessageHandler {

    int getOperationIndex();

    String getMessageHandlerName();

    void handle();

}
