package com.dzyls.chat.handler.common.impl;

import com.dzyls.chat.annotate.HandleType;
import com.dzyls.chat.annotate.Server;
import com.dzyls.chat.contants.ChatAttributeKey;
import com.dzyls.chat.contants.ChatConstants;
import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.dzyls.chat.handler.common.CommonRequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/16 21:49
 * @Version 1.0.0
 * @Description:
 */
@Server
@Component
@HandleType(type = OperationType.NAME)
public class ServerNameRequestHandler implements CommonRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerNameRequestHandler.class);

    @Resource
    private ChatContext chatContext;

    @Override
    public void handle(CommonRequest commonRequest, ChannelHandlerContext ctx) {
        String clientName = commonRequest.getMessage();
        if (!chatContext.addClient(clientName,ctx)){
            ctx.writeAndFlush(CommonRequest.generateNameRequest(ChatConstants.loginFail));
            return;
        }
        Attribute<Object> attr = ctx.channel().attr(ChatAttributeKey.CLIENT_NAME_KEY);
        chatContext.removeClient(String.valueOf(attr.get()));
        attr.set(clientName);
        ctx.writeAndFlush(CommonRequest.generateNameRequest(ChatConstants.loginSuccess));
        logger.info(clientName + " login success");
    }

}
