package com.dzyls.chat.notify.impl;

import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.notify.Notice;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.concurrent.*;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 21:03
 * @Version 1.0.0
 * @Description:
 */
public class AsyncNotice implements Notice {

    private ChatContext chatContext;

    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public AsyncNotice(ChatContext chatContext) {
        this.chatContext = chatContext;
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
