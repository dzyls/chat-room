package com.dzyls.chat.notify.impl;

import com.dzyls.chat.annotate.Server;
import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.entity.ChatMessage;
import com.dzyls.chat.notify.Notice;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 21:03
 * @Version 1.0.0
 * @Description:
 */
@Component
@Server
public class AsyncNotice implements Notice {

    @Resource
    private ChatContext chatContext;

    private BlockingQueue<ChatMessage> messageQueue = new LinkedBlockingQueue<>();

    @PostConstruct
    public void init() {
        ExecutorService pool = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 16; i++) {
            pool.execute(messageSender);
        }
    }

    @Override
    public void noticeClient(String message,String clientName) {
        ChatMessage chatMessage = new ChatMessage(clientName, message, System.currentTimeMillis());
        messageQueue.offer(chatMessage);
    }

    private Runnable messageSender = ()->{
        while (true){
            try {
                ChatMessage chatMessage = messageQueue.poll(Long.MAX_VALUE, TimeUnit.DAYS);
                chatContext.sendMessage(chatMessage);
            } catch (InterruptedException e) {
                // ignore it
            }
        }
    };


}
