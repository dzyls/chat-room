package com.dzyls.chat.handler.client;

import com.dzyls.chat.annotate.Client;
import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.entity.CommonRequest;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/20 22:37
 * @Version 1.0.0
 * @Description:
 */
@Log4j2
@Client
@Component
@HandlerOrder(order = HandlerOrder.CHECKER_ORDER)
@ChannelHandler.Sharable
public class ClientEventTrigger extends ChannelDuplexHandler{

    @Resource
    private ChatContext chatContext;

    private BeanFactory beanFactory;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        chatContext.addClient("chatServer",ctx);
        Scanner scanner = new Scanner(System.in);
        String clientName = scanner.nextLine();
        ctx.writeAndFlush(CommonRequest.generateNameRequest(clientName));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        chatContext.removeClient("chatServer");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("something error : ",cause);
    }

}
