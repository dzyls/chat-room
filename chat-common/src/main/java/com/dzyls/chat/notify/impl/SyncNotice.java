package com.dzyls.chat.notify.impl;

import com.dzyls.chat.annotate.Client;
import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.notify.Notice;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 21:01
 * @Version 1.0.0
 * @Description:
 */
@Component
@Client
public class SyncNotice implements Notice {

    @Resource
    private ChatContext chatContext;

    @Override
    public void noticeClient(String message) {
        Collection<ChannelHandlerContext> contextCollection = chatContext.getContexts();
        contextCollection.forEach(c ->{
            c.writeAndFlush(message);
        });
    }

}
