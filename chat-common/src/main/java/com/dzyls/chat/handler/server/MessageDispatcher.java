package com.dzyls.chat.handler.server;

import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.annotate.Server;
import com.dzyls.chat.contants.ChatAttributeKey;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.dzyls.chat.notify.Notice;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 22:04
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandlerOrder(order = HandlerOrder.LOWEST_ORDER)
@ChannelHandler.Sharable
@Server
public class MessageDispatcher extends SimpleChannelInboundHandler<CommonRequest> {


    @Resource
    private Notice notice;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CommonRequest msg) throws Exception {
        OperationType operationType = msg.getOperationType();
        if (operationType == OperationType.MSG){
            Attribute<Object> attr = ctx.channel().attr(ChatAttributeKey.CLIENT_NAME_KEY);
            notice.noticeClient(msg.getMessage(), String.valueOf(attr.get()));
        }
        ctx.fireChannelRead(msg);
    }
}
