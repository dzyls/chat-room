package com.dzyls.chat.handler.server;

import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.annotate.Server;
import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.util.RandomUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.dzyls.chat.contants.ChatAttributeKey.CLIENT_NAME_KEY;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/20 22:30
 * @Version 1.0.0
 * @Description:
 */
@Log4j2
@Server
@Component
@HandlerOrder(order = HandlerOrder.CHECKER_ORDER)
@ChannelHandler.Sharable
public class ServerEventTrigger extends ChannelDuplexHandler {

    @Resource
    private ChatContext chatContext;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String uuid = RandomUtils.uuid();
        ctx.channel().attr(CLIENT_NAME_KEY).setIfAbsent(uuid);
        chatContext.addClient(uuid,ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String uuid  = String.valueOf(ctx.channel().attr(CLIENT_NAME_KEY).get());
        chatContext.removeClient(uuid);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("something error : ",cause);
    }

}
