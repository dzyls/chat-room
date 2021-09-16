package com.dzyls.chat.context;

import com.dzyls.chat.entity.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 21:09
 * @Version 1.0.0
 * @Description:
 */
@Component
public class ChatContext {

    private ConcurrentHashMap<String, ChannelHandlerContext> context = new ConcurrentHashMap<>();


    public boolean addClient(String clientName, ChannelHandlerContext channelHandlerContext) {
        if (context.containsKey(clientName)) {
            return false;
        }
        context.put(clientName, channelHandlerContext);
        return true;
    }

    public ChannelHandlerContext removeClient(String clientName) {
        return context.remove(clientName);
    }

    public Collection<ChannelHandlerContext> getContextClients() {
        return context.values();
    }

    public void sendMessage(ChatMessage chatMessage) {
        context.forEach((k, v) -> {
            v.channel().writeAndFlush(chatMessage);
        });
    }
}
