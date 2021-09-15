package com.dzyls.chat.handler;

import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.entity.CommonRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 22:04
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandlerOrder(order = HandlerOrder.LOWEST_ORDER)
@ChannelHandler.Sharable
@ConditionalOnProperty(prefix = "chat",name = "role",havingValue = "server")
public class MessageDispatcher extends SimpleChannelInboundHandler<CommonRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CommonRequest msg) throws Exception {

    }
}
