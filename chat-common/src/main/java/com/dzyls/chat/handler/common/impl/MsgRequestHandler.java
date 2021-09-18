package com.dzyls.chat.handler.common.impl;

import com.dzyls.chat.annotate.HandleType;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.dzyls.chat.handler.common.CommonRequestHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 22:29
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandleType(type = OperationType.MSG)
@ChannelHandler.Sharable
public class MsgRequestHandler implements CommonRequestHandler {

    /**
     * handle chat message and dispatch
     * @param commonRequest
     * @param ctx
     */
    @Override
    public void handle(CommonRequest commonRequest, ChannelHandlerContext ctx) {
        Assert.notNull(commonRequest,"CommonRequest is null");
        System.out.println(commonRequest.getSender() +" : "+commonRequest.getMessage());
    }
}
