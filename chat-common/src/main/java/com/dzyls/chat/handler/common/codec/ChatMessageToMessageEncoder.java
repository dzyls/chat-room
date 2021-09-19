package com.dzyls.chat.handler.common.codec;

import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.contants.ChatAttributeKey;
import com.dzyls.chat.entity.ChatMessage;
import com.dzyls.chat.entity.CommonRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/16 21:22
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandlerOrder(order = HandlerOrder.CODEC_ORDER)
public class ChatMessageToMessageEncoder extends MessageToMessageEncoder<ChatMessage> {

    @Value("${expireTime:3600}")
    private long expireTime;

    @Override
    protected void encode(ChannelHandlerContext ctx, ChatMessage chatMessage, List<Object> out) throws Exception {
        String clientName = String.valueOf(ctx.channel().attr(ChatAttributeKey.CLIENT_NAME_KEY).get());
        if (!isExpireMessage(chatMessage.getCreateTime()) && !clientName.equals(chatMessage.getClientName())){
            out.add(CommonRequest.generateSendRequestWithSender(chatMessage.getMessage(),chatMessage.getClientName()));
        }
    }

    private boolean isExpireMessage(long time){
        return time - System.currentTimeMillis() > expireTime * 1000;
    }

}
