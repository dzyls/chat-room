package com.dzyls.chat.notify.impl;

import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.notify.Notice;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.*;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 21:03
 * @Version 1.0.0
 * @Description:
 */
@Component
@ConditionalOnProperty(prefix = "chat",name = "role",havingValue = "server")
public class AsyncNotice implements Notice {

    @Resource
    private ChatContext chatContext;

    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @PostConstruct
    public void init(ChatContext chatContext) {
        ExecutorService pool = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 16; i++) {
            pool.execute(messageSender);
        }
    }

    @Override
    public void noticeClient(String message) {
        messageQueue.offer(message);
    }

    private Runnable messageSender = ()->{
        while (true){
            try {
                String message = messageQueue.poll(Long.MAX_VALUE, TimeUnit.DAYS);
                sendMessage(message);
            } catch (InterruptedException e) {
                // ignore it
            }
        }
    };

    private void sendMessage(String message){
        Collection<ChannelHandlerContext> contextCollection = chatContext.getContexts();
        contextCollection.forEach(c ->{
            c.writeAndFlush(message);
        });
    }

}
