package com.dzyls.chat.input.impl;

import com.dzyls.chat.input.Input;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/8 21:47
 * @Version 1.0.0
 * @Description:
 */
@Log4j2
public abstract class AbstractInput implements Input {

    protected LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    private ChannelHandlerContext context;

    public AbstractInput(ChannelHandlerContext context) {
        this.context = context;
    }

    public void startListenMessage(){
        while (true){
            String message = null;
            try {
                message = messageQueue.take();
                context.writeAndFlush(message);
            } catch (InterruptedException e) {
                log.info("exit listen message...");
                break;
            }
        }
    }

}
