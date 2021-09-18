package com.dzyls.chat.handler.common.impl;

import com.dzyls.chat.annotate.HandleType;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.dzyls.chat.handler.common.CommonRequestHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/30 23:23
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandleType(type = OperationType.HEART_BEAT)
@ChannelHandler.Sharable
public class HeartBeatRequestHandler implements CommonRequestHandler {

    @Override
    public void handle(CommonRequest commonRequest, ChannelHandlerContext ctx) {
        System.out.println("heart beat message from : " + ctx.channel().remoteAddress());
    }

}
